package com.homegravity.Odi.domain.party.respository.custom;

import com.homegravity.Odi.domain.member.entity.Member;
import com.homegravity.Odi.domain.party.dto.PartyMemberDTO;
import com.homegravity.Odi.domain.party.entity.Party;
import com.homegravity.Odi.domain.party.entity.PartyMember;
import com.homegravity.Odi.domain.party.entity.RoleType;

import java.util.List;
import java.util.Optional;

public interface CustomPartyMember {

    int countAllPartyGuests(Party party);

    RoleType findParticipantRole(Party party, Member member);

    List<PartyMemberDTO> findAllPartyMember(Party party, RoleType role);

    Optional<PartyMember> findOrganizer(Party party);

    boolean existPartyMember(Party party, Member member);

    List<PartyMember> findAllPartyMember(Party party);

    Optional<PartyMember> findPartyPartiAndReqByMember(Party party, Member member);

    Optional<PartyMember> findByPartyAndMember(Party party, Member member);

    List<PartyMember> findAllPartyMemberAndRequester(Party party);
}
