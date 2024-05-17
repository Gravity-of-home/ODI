package com.homegravity.Odi.domain.payment.dto.toss;

import com.homegravity.Odi.domain.payment.dto.PaymentFailDto;
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

    @Getter
    public static class PaymentDetails {
        String orderName;
        String method;
        String type; // NORMAL
        Integer totalAmount;
        PaymentConfirmationStatus status;
        LocalDateTime requestedAt; // 결제 날짜
        LocalDateTime approvedAt; // 결제 승인 날짜

        @Builder
        private PaymentDetails(String orderName, String method, String type, Integer totalAmount, PaymentConfirmationStatus status, LocalDateTime requestedAt, LocalDateTime approvedAt) {
            this.orderName = orderName;
            this.method = method;
            this.type = type;
            this.totalAmount = totalAmount;
            this.status = status;
            this.requestedAt = requestedAt;
            this.approvedAt = approvedAt;
        }
    }

    @Getter
    public static class PaymentFailure {
        String errorCode;
        String message;

        @Builder
        private PaymentFailure(String errorCode, String message) {
            this.errorCode = errorCode;
            this.message = message;
        }

        public static PaymentFailure fromPaymentFailDto(PaymentFailDto paymentFailDto) {
            return builder()
                    .errorCode(paymentFailDto.getCode())
                    .message(paymentFailDto.getMessage())
                    .build();
        }
    }


}