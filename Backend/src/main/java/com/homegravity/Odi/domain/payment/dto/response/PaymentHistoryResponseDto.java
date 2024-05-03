package com.homegravity.Odi.domain.payment.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.homegravity.Odi.domain.payment.entity.Payment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Schema(description = "결제 내역 리스트 DTO")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentHistoryResponseDto {

    @Schema(description = "결제 아이디")
    private Long paymentId;

    @Schema(description = "결제 금액")
    private Integer amount;

    @Schema(description = "결제 내용")
    private String orderName;

    @Schema(description = "결제 성공 여부")
    private Boolean paymentSuccessful;

    @Schema(description = "거래 일자")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime paidAt;

    @Builder
    private PaymentHistoryResponseDto(Long paymentId, Integer amount, String orderName, Boolean paymentSuccessful, LocalDateTime paidAt) {
        this.paymentId = paymentId;
        this.amount = amount;
        this.orderName = orderName;
        this.paymentSuccessful = paymentSuccessful;
        this.paidAt = paidAt;
    }

    public static PaymentHistoryResponseDto from(Payment payment) {
        return builder()
                .paymentId(payment.getId())
                .amount(payment.getAmount())
                .orderName(payment.getOrderName())
                .paymentSuccessful(payment.getPaymentSuccessful())
                .paidAt(payment.getModifiedAt())
                .build();
    }
}
