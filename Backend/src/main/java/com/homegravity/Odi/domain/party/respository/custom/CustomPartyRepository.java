package com.homegravity.Odi.domain.party.respository.custom;

import com.homegravity.Odi.domain.party.entity.Party;

import java.util.List;

public interface CustomPartyRepository {

    Party findParty(Long partyId);

    List<Party> findAllParties();

    List<Party> findPopularParties();

}
