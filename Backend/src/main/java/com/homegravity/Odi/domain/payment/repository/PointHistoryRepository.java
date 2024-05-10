package com.homegravity.Odi.domain.payment.repository;

import com.homegravity.Odi.domain.member.entity.Member;
import com.homegravity.Odi.domain.payment.entity.PointHistory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointHistoryRepository extends JpaRepository<PointHistory, Long> {
    Slice<PointHistory> findAllByMemberAndDeletedAtIsNull(Member member, Pageable pageable);
}
