package com.homegravity.Odi.domain.party.respository.custom;

import com.homegravity.Odi.domain.party.entity.Party;
import com.homegravity.Odi.domain.party.entity.PartyBoardStats;

public interface CustomPartyBoardStatsRepository {

    PartyBoardStats findPartyBoardStats(Party party);

}
