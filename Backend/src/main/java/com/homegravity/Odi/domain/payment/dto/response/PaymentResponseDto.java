package com.homegravity.Odi.domain.payment.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.homegravity.Odi.domain.payment.entity.Payment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Schema(description = "결제 요청 응답 DTO")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentResponseDto {

    @Schema(description = "결제 타입 (카드/현금/포인트)")
    private String payType;

    @Schema(description = "결제 금액")
    private Long amount;

    @Schema(description = "주문 아이디")
    private String orderId;

    @Schema(description = "주문 내용(구매상품)")
    private String orderName;

    @Schema(description = "구매자 이메일 주소")
    private String customerEmail;

    @Schema(description = "구매자 명")
    private String customerName;

    @Schema(description = "거래 생성 일자")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @Builder
    private PaymentResponseDto(String payType, Long amount, String orderId, String orderName, String customerEmail, String customerName, LocalDateTime createdAt) {
        this.payType = payType;
        this.amount = amount;
        this.orderId = orderId;
        this.orderName = orderName;
        this.customerEmail = customerEmail;
        this.customerName = customerName;
        this.createdAt = createdAt;
    }

    public static PaymentResponseDto from(Payment payment) {
        return builder()
                .payType(payment.getPayType().getDescription())
                .amount(payment.getAmount())
                .orderId(payment.getOrderId())
                .orderName(payment.getOrderName())
                .customerEmail(payment.getCustomer().getEmail())
                .customerName(payment.getCustomer().getName())
                .createdAt(payment.getCreatedAt())
                .build();
    }
}
