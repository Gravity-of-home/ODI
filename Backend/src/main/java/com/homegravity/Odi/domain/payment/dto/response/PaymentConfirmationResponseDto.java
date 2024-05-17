package com.homegravity.Odi.domain.payment.dto.response;

import com.homegravity.Odi.domain.payment.dto.toss.PaymentConfirmation.PaymentFailure;
import com.homegravity.Odi.domain.payment.entity.PaymentState;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "결제 승인 결과 DTO")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentConfirmationResponseDto {

    @Schema(description = "결과 상태")
    private PaymentState state;

    @Schema(description = "결과 메세지")
    private String message;

    @Schema(description = "(상태가 실패라면) 실패 이유")
    private PaymentFailure failure;

    @Builder
    private PaymentConfirmationResponseDto(PaymentState state, String message, PaymentFailure failure) {
        this.state = state;
        this.message = message;
        this.failure = failure;
    }

    public static PaymentConfirmationResponseDto of(PaymentState state) {
        return builder()
                .state(state)
                .message(getMessage(state))
                .build();
    }

    public static PaymentConfirmationResponseDto of(PaymentState state, PaymentFailure failure) {
        return builder()
                .state(state)
                .message(getMessage(state))
                .failure(failure)
                .build();
    }

    private static String getMessage(PaymentState state) {
        switch (state) {
            case SUCCESS -> {
                return "결제 처리에 성공했습니다";
            }
            case FAILURE -> {
                return "결제 처리에 실패했습니다";
            }
            case UNKNOWN -> {
                return "결제 처리 중 알 수 없는 에러가 발생했습니다";
            }
        }
        return null;
    }
}
