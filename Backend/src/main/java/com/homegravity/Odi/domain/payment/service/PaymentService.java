package com.homegravity.Odi.domain.payment.service;

import com.homegravity.Odi.domain.member.entity.Member;
import com.homegravity.Odi.domain.payment.dto.PaymentFailDto;
import com.homegravity.Odi.domain.payment.dto.request.PaymentRequestDto;
import com.homegravity.Odi.domain.payment.dto.request.PaymentSuccessRequestDto;
import com.homegravity.Odi.domain.payment.dto.response.PaymentHistoryResponseDto;
import com.homegravity.Odi.domain.payment.dto.response.PaymentResponseDto;
import com.homegravity.Odi.domain.payment.dto.response.PaymentSuccessResponseDto;
import com.homegravity.Odi.domain.payment.entity.Payment;
import com.homegravity.Odi.domain.payment.repository.PaymentRepository;
import com.homegravity.Odi.global.response.error.ErrorCode;
import com.homegravity.Odi.global.response.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final WebClient tossPaymentWebClient;

    private final PointService pointService;

    // 토스 결제 요청: 토스 PSP로 결제 요청 전 결제 서버에 등록
    public PaymentResponseDto requestTossPayment(Member member, PaymentRequestDto requestDto) {

        Payment payment = paymentRepository.save(Payment.createNewPayment(requestDto.getPayType(), requestDto.getAmount(), requestDto.getOrderName(), member));
        return PaymentResponseDto.from(payment);
    }

    // 결제 데이터 검증
    public PaymentSuccessResponseDto successTossPayment(PaymentSuccessRequestDto requestDto) {

        // 존재하는 결제인지 확인
        Payment payment = verifyPayment(requestDto.getOrderId());

        // 토스에 구매자의 결제 응답 검증 요청
        PaymentSuccessResponseDto responseDto = requestPaymentConfirm(requestDto);

        // 결제정보 업데이트
        payment.updatePaymentSuccessInfo(payment.getPaymentKey());
        pointService.chargePoint(payment);

        return responseDto;
    }

    // DB에 존재하는 결제 정보인지 확인
    public Payment verifyPayment(String orderId) {
        return paymentRepository.findByOrderIdAndDeletedAtIsNull(orderId).orElseThrow(() -> new BusinessException(ErrorCode.ORDER_ID_NOT_EXIST, "존재하지 않는 결제 정보입니다."));
    }

    // 토스에 결제 검증 및 승인 요청
    public PaymentSuccessResponseDto requestPaymentConfirm(PaymentSuccessRequestDto requestDto) {

        return tossPaymentWebClient.post()
                .body(Mono.just(requestDto), PaymentRequestDto.class)
                .retrieve()
                .bodyToMono(PaymentSuccessResponseDto.class)
                .block();
    }

    // 결제 실패 정보 업데이트
    public PaymentFailDto failTossPayment(PaymentFailDto requestDto) {

        // 존재하는 결제 요청이었는지 확인
        Payment payment = verifyPayment(requestDto.getOrderId());
        payment.updatePaymentFailInfo(requestDto.getMessage());

        return requestDto;
    }

    // 결제 내역 조회 - 최신순
    public Slice<PaymentHistoryResponseDto> getPaymentHistory(Member member, Pageable pageable) {

        Pageable sortPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Order.desc("modifiedAt")));
        return paymentRepository.findAllByCustomer(member, sortPageable).map(PaymentHistoryResponseDto::from);
    }
}
