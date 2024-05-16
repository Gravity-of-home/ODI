package com.homegravity.Odi.domain.payment.dto.toss;

import com.homegravity.Odi.domain.payment.dto.PaymentFailDto;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PaymentFailure {
    String errorCode;
    String message;

    @Builder
    public PaymentFailure(String errorCode, String message) {
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