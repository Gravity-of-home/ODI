package com.homegravity.Odi.domain.member.repository;

import com.homegravity.Odi.domain.member.entity.MemberReview;
import com.homegravity.Odi.domain.member.repository.custom.CustomMemberReviewRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberReviewRepository extends JpaRepository<MemberReview, Long>, CustomMemberReviewRepository {
}
