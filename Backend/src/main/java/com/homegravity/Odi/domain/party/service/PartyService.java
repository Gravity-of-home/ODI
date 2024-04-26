package com.homegravity.Odi.domain.party.service;

import com.homegravity.Odi.domain.member.entity.Member;
import com.homegravity.Odi.domain.party.dto.PartyMemberDTO;
import com.homegravity.Odi.domain.party.dto.request.PartyRequestDTO;
import com.homegravity.Odi.domain.party.dto.response.PartyResponseDTO;
import com.homegravity.Odi.domain.party.entity.Party;
import com.homegravity.Odi.domain.party.entity.PartyBoardStats;
import com.homegravity.Odi.domain.party.entity.PartyMember;
import com.homegravity.Odi.domain.party.entity.RoleType;
import com.homegravity.Odi.domain.party.respository.PartyBoardStatsRepository;
import com.homegravity.Odi.domain.party.respository.PartyMemberRepository;
import com.homegravity.Odi.domain.party.respository.PartyRepository;
import com.homegravity.Odi.global.response.error.ErrorCode;
import com.homegravity.Odi.global.response.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PartyService {

    private final PartyRepository partyRepository;
    private final PartyBoardStatsRepository partyBoardStatsRepository;
    private final PartyMemberRepository partyMemberRepository;

    @Transactional
    public Long createParty(PartyRequestDTO partyRequestDTO, Member member) {

        Party party = partyRepository.save(Party.from(partyRequestDTO));
        Long requestCount = partyMemberRepository.countAllPartyGuests(party); // 파티 참여 신청자 수

        int requestCountInt = 0; // 파티 요청자 수, 요청자 수가 많을 수록 인기 있는 파티
        if (requestCount != null) {
            requestCountInt = requestCount.intValue();
        }

        PartyBoardStats partyBoardStats = PartyBoardStats.of(0, requestCountInt); // 생성시 조회수 초기화
        partyMemberRepository.save(PartyMember.of(RoleType.ORGANIZER, false, party, member));
        partyBoardStats.updateParty(party);

        return party.getId();
    }

    @Transactional
    public PartyResponseDTO getPartyDetail(Long partyId, Member member) {

        Party party = partyRepository.findParty(partyId);
        if (party == null) {
            throw BusinessException.builder().errorCode(ErrorCode.NOT_FOUND_ERROR).message(ErrorCode.NOT_FOUND_ERROR.getMessage()).build();
        }

        PartyBoardStats partyBoardStats = partyBoardStatsRepository.findPartyBoardStats(party);

        partyBoardStats.addViewCount(); // 조회수 증가

        RoleType role = partyMemberRepository.findParticipantRole(member);

        // 파티원, 파티 참여 신정자 목록 조회
        List<PartyMemberDTO> guests = null;
        // 방장이라면?
        if (role.equals(RoleType.ORGANIZER)) {
            guests = partyMemberRepository.findAllPartyGuests(party);
        }

        List<PartyMemberDTO> participants = partyMemberRepository.findAllPartyParticipants(party);

        return PartyResponseDTO.of(party, partyBoardStats, role, participants, guests);
    }
}
