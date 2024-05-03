package com.homegravity.Odi.domain.payment.controller;

import com.homegravity.Odi.domain.member.entity.Member;
import com.homegravity.Odi.domain.payment.dto.PaymentFailDto;
import com.homegravity.Odi.domain.payment.dto.request.PaymentRequestDto;
import com.homegravity.Odi.domain.payment.dto.request.PaymentSuccessRequestDto;
import com.homegravity.Odi.domain.payment.dto.response.PaymentHistoryResponseDto;
import com.homegravity.Odi.domain.payment.dto.response.PaymentResponseDto;
import com.homegravity.Odi.domain.payment.dto.response.PaymentSuccessResponseDto;
import com.homegravity.Odi.domain.payment.service.PaymentService;
import com.homegravity.Odi.global.response.success.ApiResponse;
import com.homegravity.Odi.global.response.success.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "결제", description = "토스페이 기반 결제 요청 및 승인")
@RestController
@Validated
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @Operation(summary = "결제 요청 1차", description = "토스 페이 결제 요청 1차. 본 API 호출 후 토스페이로 결제 요청을 해야 합니다.")
    @PostMapping()
    public ApiResponse<PaymentResponseDto> requestTossPayment(@AuthenticationPrincipal Member member, @RequestBody @Valid PaymentRequestDto requestDto) {

        return ApiResponse.of(SuccessCode.PAYMENT_REQUEST_SUCCESS, paymentService.requestTossPayment(member, requestDto));
    }

    @Operation(summary = "구매자 PSP 결제 성공", description = "구매자의 PSP 결제 성공에 대해 결제 데이터를 업데이트 합니다.")
    @PostMapping("/success")
    public ApiResponse<PaymentSuccessResponseDto> successTossPayment(@RequestBody @Valid PaymentSuccessRequestDto requestDto) {

        return ApiResponse.of(SuccessCode.PAYMENT_CONFIRM_SUCCESS, paymentService.successTossPayment(requestDto));
    }

    @Operation(summary = "구매자 PSP 결제 실패", description = "구매자의 PSP 결제 실패에 대해 결제 정보를 업데이트 합니다.")
    @PostMapping("/fail")
    public ApiResponse<PaymentFailDto> failTossPayment(@RequestBody @Valid PaymentFailDto requestDto) {

        return ApiResponse.of(SuccessCode.PAYMENT_FAIL_UPDATE_SUCCESS, paymentService.failTossPayment(requestDto));
    }

    @Operation(summary = "포인트 충전 내역 조회", description = "현재 유저의 포인트 거래 내역을 조회합니다.")
    @GetMapping("/history")
    public ApiResponse<Slice<PaymentHistoryResponseDto>> getPaymentHistory(@AuthenticationPrincipal Member member, Pageable pageable) {

        return ApiResponse.of(SuccessCode.PAYMENT_HISTORY_GET_SUCCESS, paymentService.getPaymentHistory(member, pageable));
    }
}
