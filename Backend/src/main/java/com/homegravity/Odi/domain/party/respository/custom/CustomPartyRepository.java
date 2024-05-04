package com.homegravity.Odi.domain.party.respository.custom;

import com.homegravity.Odi.domain.party.dto.PartyDTO;
import com.homegravity.Odi.domain.party.dto.request.SelectPartyRequestDTO;
import com.homegravity.Odi.domain.party.entity.Party;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface CustomPartyRepository {

    Party findParty(Long partyId);

    Slice<PartyDTO> findAllParties(Pageable pageable, SelectPartyRequestDTO requestDTO);

}
