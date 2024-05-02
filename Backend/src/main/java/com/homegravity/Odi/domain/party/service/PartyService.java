package com.homegravity.Odi.domain.party.service;

import com.homegravity.Odi.domain.map.service.MapService;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PartyService {

    private final PartyRepository partyRepository;
    private final PartyBoardStatsRepository partyBoardStatsRepository;
    private final PartyMemberRepository partyMemberRepository;
    private final MapService mapService;

    @Transactional
    public Long createParty(PartyRequestDTO partyRequestDTO, Member member) {

        Party party = partyRepository.save(Party.of(partyRequestDTO, member.getGender()));

        PartyBoardStats partyBoardStats = PartyBoardStats.of(0, 0); // 생성시 조회수 초기화
        partyMemberRepository.save(PartyMember.of(RoleType.ORGANIZER, false, party, member));
        partyBoardStats.updateParty(party);

        return party.getId();
    }

    @Transactional
    public PartyResponseDTO getPartyDetail(Long partyId, Member member) {

        Party party = partyRepository.findParty(partyId);
        if (party == null) { // 파티가 없다면 삭제되었거나 요청에 문제가 있음
            throw BusinessException.builder().errorCode(ErrorCode.NOT_FOUND_ERROR).message(ErrorCode.NOT_FOUND_ERROR.getMessage()).build();
        }

        // 조회수, 신청자 수 갱신
        PartyBoardStats partyBoardStats = partyBoardStatsRepository.findPartyBoardStats(party);
        partyBoardStats.addViewCount(); // 조회수 증가
        
        // 파티 참여 신청자 수 조회
        partyBoardStats.updateRequestCount(partyMemberRepository.countAllPartyGuests(party));

        // 파티원, 파티 참여 신정자 목록 조회
        RoleType role = partyMemberRepository.findParticipantRole(party, member);

        List<PartyMemberDTO> guests = null;
        if (role != null && role.equals(RoleType.ORGANIZER)) { // 방장이라면 신청자 목록도 조회 가능
            guests = partyMemberRepository.findAllPartyMember(party, RoleType.REQUESTER);
        }

        List<PartyMemberDTO> participants = partyMemberRepository.findAllPartyMember(party, RoleType.PARTICIPANT);

        // Naver Map API 호출
        String pathInfo = mapService.getNaverPathInfo(party.getDeparturesLocation().getX(), party.getDeparturesLocation().getY(), party.getArrivalsLocation().getX(), party.getArrivalsLocation().getY());

        return PartyResponseDTO.of(party, partyBoardStats, role, participants, guests, pathInfo);
    }
}
