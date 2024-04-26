package com.homegravity.Odi.domain.party.respository;

import com.homegravity.Odi.domain.party.entity.PartyBoardStats;
import com.homegravity.Odi.domain.party.respository.custom.CustomPartyBoardStatsRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartyBoardStatsRepository extends JpaRepository<PartyBoardStats, Long>, CustomPartyBoardStatsRepository {
}
