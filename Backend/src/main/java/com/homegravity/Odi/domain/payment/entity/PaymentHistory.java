package com.homegravity.Odi.domain.payment.entity;

import com.homegravity.Odi.global.entity.BaseBy;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentHistory extends BaseBy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_history_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @Column(name = "previous_state")
    @Enumerated(EnumType.STRING)
    private PaymentState previousState;

    @Column(name = "new_state")
    @Enumerated(EnumType.STRING)
    private PaymentState newState;

    @Column(name = "reason")
    private String reason; // 상태 변경의 이유

    @Builder
    private PaymentHistory(Payment payment, PaymentState previousState, PaymentState newState, String reason) {
        this.payment = payment;
        this.previousState = previousState;
        this.newState = newState;
        this.reason = reason;
    }

    public static PaymentHistory of(Payment payment, PaymentState previousState, PaymentState newState, String reason) {
        return builder()
                .payment(payment)
                .previousState(previousState)
                .newState(newState)
                .reason(reason)
                .build();
    }
}
