package com.homegravity.Odi.domain.payment.dto.toss;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class PaymentConfirmation {

    private String paymentKey;
    private String orderId;
    private PaymentDetails paymentDetails;
    private PaymentFailure failure;
    private Boolean isSuccess; // 성공 여부
    private Boolean isFailure; // 실패 여부
    private Boolean isUnknown; // 알 수 없는 상태인지 여부
    private Boolean isRetryable; // 재시도 가능 여부

    @Builder
    private PaymentConfirmation(String paymentKey, String orderId, PaymentDetails paymentDetails, PaymentFailure failure, Boolean isSuccess, Boolean isFailure, Boolean isUnknown, Boolean isRetryable) {
        this.paymentKey = paymentKey;
        this.orderId = orderId;
        this.paymentDetails = paymentDetails;
        this.failure = failure;
        this.isSuccess = isSuccess;
        this.isFailure = isFailure;
        this.isUnknown = isUnknown;
        this.isRetryable = isRetryable;
    }

    public static PaymentConfirmation fromResponseDto(PSPConfirmationResponseDto responseDto) {
        return builder()
                .paymentKey(responseDto.paymentKey)
                .orderId(responseDto.orderId)
                .paymentDetails(PaymentDetails.builder()
                        .orderName(responseDto.orderName)
                        .method(responseDto.method)
                        .type(responseDto.type)
                        .totalAmount(Integer.parseInt(responseDto.totalAmount))
                        .status(PaymentConfirmationStatus.valueOf(responseDto.status))
                        .requestedAt(LocalDateTime.parse(responseDto.requestedAt, DateTimeFormatter.ISO_OFFSET_DATE_TIME))
                        .approvedAt(LocalDateTime.parse(responseDto.approvedAt, DateTimeFormatter.ISO_OFFSET_DATE_TIME))
                        .build())
                .isSuccess(true)
                .isFailure(false)
                .isUnknown(false)
                .isRetryable(false)
                .build();
    }
}