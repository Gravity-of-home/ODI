package com.homegravity.Odi.domain.party.respository;

import com.homegravity.Odi.domain.party.entity.Party;
import com.homegravity.Odi.domain.party.entity.PartySettlement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PartySettlementRepository extends JpaRepository<PartySettlement, Long> {

    Optional<PartySettlement> findByPartyAndDeletedAtIsNull(Party party);
}
