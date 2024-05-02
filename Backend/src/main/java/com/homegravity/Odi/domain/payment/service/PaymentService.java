package com.homegravity.Odi.domain.payment.service;

import com.homegravity.Odi.domain.member.entity.Member;
import com.homegravity.Odi.domain.payment.dto.request.PaymentRequestDto;
import com.homegravity.Odi.domain.payment.dto.response.PaymentResponseDto;
import com.homegravity.Odi.domain.payment.entity.Payment;
import com.homegravity.Odi.domain.payment.repository.PaymentRepository;
import com.homegravity.Odi.global.config.TossPaymentConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final TossPaymentConfig tossPaymentConfig;

    // 토스 결제 요청: 토스 PSP로 결제 요청 전 결제 서버에 등록
    public PaymentResponseDto requestTossPayment(Member member, PaymentRequestDto requestDto) {

        Payment payment = paymentRepository.save(Payment.createNewPayment(requestDto.getPayType(), requestDto.getAmount(), requestDto.getOrderName(), member));
        return PaymentResponseDto.from(payment);
    }
}
