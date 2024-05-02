package com.homegravity.Odi.domain.party.respository.custom;

import com.homegravity.Odi.domain.member.entity.Member;
import com.homegravity.Odi.domain.party.dto.PartyMemberDTO;
import com.homegravity.Odi.domain.party.entity.Party;
import com.homegravity.Odi.domain.party.entity.RoleType;

import java.util.List;

public interface CustomPartyMember {

    int countAllPartyGuests(Party party);

    RoleType findParticipantRole(Party party, Member member);

    List<PartyMemberDTO> findAllPartyMember(Party party, RoleType role);

}
