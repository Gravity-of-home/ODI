package com.homegravity.Odi.domain.party.respository;

import com.homegravity.Odi.domain.party.entity.Party;
import com.homegravity.Odi.domain.party.respository.custom.CustomPartyRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PartyRepository extends JpaRepository<Party, Long>, CustomPartyRepository {

    Optional<Party> findByRoomIdAndDeletedAtIsNull(String roomId);
}
