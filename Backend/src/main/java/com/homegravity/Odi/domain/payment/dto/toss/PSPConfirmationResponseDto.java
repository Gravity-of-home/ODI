package com.homegravity.Odi.domain.payment.dto.toss;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Setter;

@Schema(description = "결제 검증 및 승인 성공 DTO by toss")
@Setter
public class PSPConfirmationResponseDto {

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
    String requestedAt; // 결제 날짜
    String approvedAt; // 결제 승인 날짜
    String useEscrow;
    String cultureExpense;
    String type; // NORMAL
}
