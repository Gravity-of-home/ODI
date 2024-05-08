package com.homegravity.Odi.domain.member.repository.custom;

import com.homegravity.Odi.domain.member.entity.Member;
import com.homegravity.Odi.domain.member.entity.MemberReview;
import com.homegravity.Odi.domain.member.entity.QMemberReview;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CustomMemberReviewRepositoryImpl implements CustomMemberReviewRepository{
    private final JPAQueryFactory jpaQueryFactory;

    //동승자 리뷰 리스트 조회(해당 파티에 reviewee 기준)
    @Override
    public List<MemberReview> findAllMemberReview(Member member, Long partyId) {
        QMemberReview qMemberReview = QMemberReview.memberReview;

        return jpaQueryFactory.selectFrom(qMemberReview)
                .where(qMemberReview.partyId.eq(partyId)
                        .and(qMemberReview.reviewee.eq(member))
                        .and(qMemberReview.deletedAt.isNull()))
                .fetch();
    }
}
