package com.homegravity.Odi.domain.party.respository;

import com.homegravity.Odi.domain.party.entity.Party;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartyRepository extends JpaRepository<Party, Long> {
}
