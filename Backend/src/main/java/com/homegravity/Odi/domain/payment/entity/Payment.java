package com.homegravity.Odi.domain.payment.entity;

import com.homegravity.Odi.domain.member.entity.Member;
import com.homegravity.Odi.global.entity.BaseTime;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE payment SET deleted_at = NOW() where payment_id = ?")
public class Payment extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long id;

    @Column(name = "pay_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private PayType payType;

    @Column(name = "amount", nullable = false)
    private Integer amount;

    @Column(name = "order_name", nullable = false)
    private String orderName;

    @Column(name = "order_id", nullable = false)
    private String orderId;

    @Column(name = "payment_state")
    private PaymentState paymentState;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Member customer;

    @Column(name = "approved_at") // 결제 승인 시각
    private LocalDateTime approvedAt;

    @Column(name = "payment_key")
    private String paymentKey;

    @Column(name = "ledger_updated")
    private Boolean ledgerUpdated; // 장부 업데이트 여부

    @Column(name = "failed_count")
    private Integer failedCount; // 결제 실패 카운트

    @Builder
    private Payment(PayType payType, Integer amount, String orderName, String orderId, PaymentState paymentState, Member customer, LocalDateTime approvedAt, String paymentKey, Boolean ledgerUpdated, Integer failedCount) {
        this.payType = payType;
        this.amount = amount;
        this.orderName = orderName;
        this.orderId = orderId;
        this.paymentState = paymentState;
        this.customer = customer;
        this.approvedAt = approvedAt;
        this.paymentKey = paymentKey;
        this.ledgerUpdated = ledgerUpdated;
        this.failedCount = failedCount;
    }

    public static Payment createNewPayment(PayType payType, Integer amount, String orderName, Member member, String orderInfoString) {
        return Payment.builder()
                .payType(payType)
                .amount(amount)
                .orderName(orderName)
                .orderId(UUID.nameUUIDFromBytes(orderInfoString.getBytes()).toString())
                .paymentState(PaymentState.NOT_STARTED)
                .customer(member)
                .ledgerUpdated(false)
                .failedCount(0)
                .build();
    }

    public void updatePaymentSuccessInfo() {

        this.paymentState = PaymentState.SUCCESS;
    }

    public void updatePaymentFailInfo(String failReason) {

        this.paymentState = PaymentState.FAILURE;
    }

    public void updatePaymentState(PaymentState paymentState) {
        this.paymentState = paymentState;
    }

    public void updatePaymentKey(String paymentKey) {
        this.paymentKey = paymentKey;
    }

}
