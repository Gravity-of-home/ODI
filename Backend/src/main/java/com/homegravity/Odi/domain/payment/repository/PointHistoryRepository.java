package com.homegravity.Odi.domain.payment.repository;

import com.homegravity.Odi.domain.payment.entity.PointHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointHistoryRepository extends JpaRepository<PointHistory, Long> {
}
