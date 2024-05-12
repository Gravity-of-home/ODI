package com.homegravity.Odi.domain.member.repository.custom;

import com.homegravity.Odi.domain.member.entity.Member;
import com.homegravity.Odi.domain.member.entity.MemberReview;
import com.homegravity.Odi.domain.party.entity.PartyMember;

import java.util.List;

public interface CustomMemberReviewRepository {

    List<MemberReview> findAllMemberReview(Member member, Long partyId);
}
