package com.homegravity.Odi.domain.member.entity;

import com.homegravity.Odi.domain.member.dto.MemberBrixDTO;
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
@SQLDelete(sql = "UPDATE member_review SET deleted_at = NOW() WHERE member_review_id =?")
public class MemberReview extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_review_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "reviewee_id", nullable = false)
    private Member reviewee;// 리뷰 받는 사람

    @Column(name = "party_id", nullable = false)
    private Long partyId;

    @Column(name = "reviewer_id", nullable = false)
    private Long reviewerId;//리뷰 해주는 사람

    @Column(name = "kind_score", nullable = false)
    private Integer kindScore;//친절 매너 점수

    @Column(name = "promise_score", nullable = false)
    private Integer promiseScore;//시간 약속 점수

    @Column(name = "fast_chat_score", nullable = false)
    private Integer fastChatScore;//빠른 응답 점수

    @Builder
    private MemberReview(Member reviewee, Long partyId, Long reviewerId, Integer kindScore, Integer promiseScore, Integer fastChatScore) {
        this.reviewee = reviewee;
        this.partyId = partyId;
        this.reviewerId = reviewerId;
        this.kindScore = kindScore;
        this.promiseScore = promiseScore;
        this.fastChatScore = fastChatScore;
    }


    public static MemberReview of(MemberBrixDTO memberBrixDTO, Member reviewee, Long partyId, Member reviewer) {
        return builder()
                .reviewee(reviewee)
                .partyId(partyId)
                .reviewerId(reviewer.getId())
                .kindScore(memberBrixDTO.getKindScore())
                .promiseScore(memberBrixDTO.getPromiseScore())
                .fastChatScore(memberBrixDTO.getFastChatScore())
                .build();
    }
}
