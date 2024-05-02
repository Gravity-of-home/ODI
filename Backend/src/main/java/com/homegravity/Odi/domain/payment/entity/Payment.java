package com.homegravity.Odi.domain.payment.entity;

import com.homegravity.Odi.domain.member.entity.Member;
import com.homegravity.Odi.global.entity.BaseTime;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

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
    private Long amount;

    @Column(name = "order_name", nullable = false)
    private String orderName;

    @Column(name = "order_id", nullable = false)
    private String orderId;

    @Column(name = "payment_successful")
    private Boolean paymentSuccessful;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Member customer;

    @Column(name = "payment_key")
    private String paymentKey;

    @Column(name = "fail_reason")
    private String failReason;

    @Column(name = "is_canceled")
    private Boolean isCanceled;

    @Column(name = "cancel_reason")
    private String cancelReason;

    @Builder
    private Payment(PayType payType, Long amount, String orderName, String orderId, Boolean paymentSuccessful, Member customer, String paymentKey, String failReason, Boolean isCanceled, String cancelReason) {
        this.payType = payType;
        this.amount = amount;
        this.orderName = orderName;
        this.orderId = orderId;
        this.paymentSuccessful = paymentSuccessful;
        this.customer = customer;
        this.paymentKey = paymentKey;
        this.failReason = failReason;
        this.isCanceled = isCanceled;
        this.cancelReason = cancelReason;
    }

    public static Payment createNewPayment(PayType payType, Long amount, String orderName, Member member) {
        return Payment.builder()
                .payType(payType)
                .amount(amount)
                .orderName(orderName)
                .orderId(UUID.randomUUID().toString())
                .customer(member)
                .build();
    }

    public void updatePaymentSuccessInfo(String paymentKey) {

        this.paymentKey = paymentKey;
        this.paymentSuccessful = true;
    }

}
