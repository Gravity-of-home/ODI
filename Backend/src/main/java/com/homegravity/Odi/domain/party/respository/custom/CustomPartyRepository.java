package com.homegravity.Odi.domain.party.respository.custom;

import com.homegravity.Odi.domain.party.dto.request.SelectPartyRequestDTO;
import com.homegravity.Odi.domain.party.entity.Party;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomPartyRepository {

    Party findParty(Long partyId);

    List<Party> findAllParties(Pageable pageable, SelectPartyRequestDTO requestDTO);

    List<Party> findPopularParties();

}
