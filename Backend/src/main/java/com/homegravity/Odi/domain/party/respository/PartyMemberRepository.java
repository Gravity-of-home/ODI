package com.homegravity.Odi.domain.party.respository;

import com.homegravity.Odi.domain.party.entity.PartyMember;
import com.homegravity.Odi.domain.party.respository.custom.CustomPartyMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartyMemberRepository extends JpaRepository<PartyMember, Long>, CustomPartyMember {
}
