package com.homegravity.Odi.domain.payment.entity;

import com.homegravity.Odi.domain.member.entity.Member;
import com.homegravity.Odi.global.entity.BaseTime;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE point_history SET deleted_at = NOW() where point_history_id = ?")
public class PointHistory extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "point_history_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "party_id")
    private Long partyId;

    @Column(name = "payment_id")
    private Long paymentId;

    @Column(name = "point_history_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private PointHistoryType type;

    @Column(name = "content")
    private String content;

    @Column(name = "detail_content")
    private String detailContent;

    @Column(name = "amount")
    private Integer amount;

    @Builder
    private PointHistory(Member member, Long partyId, Long paymentId, PointHistoryType type, String content, String detailContent, Integer amount) {
        this.member = member;
        this.partyId = partyId;
        this.paymentId = paymentId;
        this.type = type;
        this.content = content;
        this.detailContent = detailContent;
        this.amount = amount;
    }

    public static PointHistory createSettleHistory(Member member, Long partyId, String content, String detailContent, PointHistoryType type, Integer amount) {

        return builder()
                .member(member)
                .partyId(partyId)
                .content(content)
                .detailContent(detailContent)
                .type(type)
                .amount(amount)
                .build();
    }

    public static PointHistory createPaymentHistory(Member member, Long paymentId, String content, Integer amount) {

        return builder()
                .member(member)
                .paymentId(paymentId)
                .content(content)
                .type(PointHistoryType.CHARGE)
                .amount(amount)
                .build();
    }

}
