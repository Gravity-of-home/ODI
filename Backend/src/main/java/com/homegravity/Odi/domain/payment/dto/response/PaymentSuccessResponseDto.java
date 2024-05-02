package com.homegravity.Odi.domain.payment.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "결제 검증 및 승인 성공 DTO by toss")
@Getter
@Setter
public class PaymentSuccessResponseDto {

    String mid;
    String version;
    String paymentKey;
    String orderId;
    String orderName;
    String currency;
    String method;
    String totalAmount;
    String balanceAmount;
    String suppliedAmount;
    String vat;
    String status;
    String requestedAt;
    String approvedAt;
    String useEscrow;
    String cultureExpense;
    String type;
}
