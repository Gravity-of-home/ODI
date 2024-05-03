package com.homegravity.Odi.domain.payment.repository;

import com.homegravity.Odi.domain.member.entity.Member;
import com.homegravity.Odi.domain.payment.entity.Payment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByOrderIdAndDeletedAtIsNull(String orderId);

    Slice<Payment> findAllByCustomer(Member member, Pageable pageable);
}
