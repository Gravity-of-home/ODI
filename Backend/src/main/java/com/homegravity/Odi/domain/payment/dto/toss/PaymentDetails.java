package com.homegravity.Odi.domain.payment.dto.toss;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PaymentDetails {
    String orderName;
    String method;
    String type; // NORMAL
    Integer totalAmount;
    PaymentConfirmationStatus status;
    LocalDateTime requestedAt; // 결제 날짜
    LocalDateTime approvedAt; // 결제 승인 날짜

    @Builder
    public PaymentDetails(String orderName, String method, String type, Integer totalAmount, PaymentConfirmationStatus status, LocalDateTime requestedAt, LocalDateTime approvedAt) {
        this.orderName = orderName;
        this.method = method;
        this.type = type;
        this.totalAmount = totalAmount;
        this.status = status;
        this.requestedAt = requestedAt;
        this.approvedAt = approvedAt;
    }
}

