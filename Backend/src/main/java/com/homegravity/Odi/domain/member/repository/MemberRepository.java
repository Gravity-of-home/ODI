package com.homegravity.Odi.domain.member.repository;

import com.homegravity.Odi.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByIdAndDeletedAtIsNull(Long id);

    Optional<Member> findByProviderCodeAndDeletedAtIsNull(String providerCode);

    boolean existsByNicknameAndDeletedAtIsNull(String nickname);


}
