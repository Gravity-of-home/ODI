package com.homegravity.Odi.domain.member.repository;

import com.homegravity.Odi.domain.member.entity.MemberReview;
import com.homegravity.Odi.domain.party.respository.custom.CustomPartyBoardStatsRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberReviewRepository extends JpaRepository<MemberReview, Long>, CustomPartyBoardStatsRepository {
}
