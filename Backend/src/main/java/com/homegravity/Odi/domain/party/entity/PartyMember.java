package com.homegravity.Odi.domain.party.entity;

import com.homegravity.Odi.domain.member.entity.Member;
import com.homegravity.Odi.global.entity.BaseBy;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE party_member SET deleted_at = NOW() where party_member_id = ?")
public class PartyMember extends BaseBy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "party_member_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private RoleType role;

    @Column(name = "settle_amount")
    private Integer settleAmount;

    @Column(name = "paid_amount")
    private Integer paidAmount;

    @Column(name = "is_paid")
    private Boolean isPaid;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "party_id")
    private Party party;

    @Builder
    private PartyMember(RoleType role, Boolean isPaid, Integer settleAmount, Integer paidAmount, Party party, Member member) {
        this.role = role;
        this.isPaid = isPaid;
        this.settleAmount = settleAmount;
        this.paidAmount = paidAmount;
        this.party = party;
        this.member = member;
    }

    public static PartyMember of(RoleType role, Boolean isPaid, Party party, Member member) {
        return PartyMember.builder()
                .role(role)
                .isPaid(isPaid)
                .settleAmount(0)
                .paidAmount(0)
                .party(party)
                .member(member)
                .build();
    }

    public void updateIsPaid(Boolean isPaid) {
        this.isPaid = isPaid;
    }

    public void updatePaidInfo(Integer paidAmount) {
        this.paidAmount = paidAmount;
    }

    public void updatePartyRole(RoleType roleType) {
        this.role = roleType;
    }

    public void updateSettleAmount(Integer settleAmount) {
        this.settleAmount = settleAmount;
    }

}
