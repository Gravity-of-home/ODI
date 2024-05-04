package com.homegravity.Odi.domain.party.controller;

import com.homegravity.Odi.domain.member.entity.Member;
import com.homegravity.Odi.domain.party.dto.PartyDTO;
import com.homegravity.Odi.domain.party.dto.request.PartyRequestDTO;
import com.homegravity.Odi.domain.party.dto.request.SelectPartyRequestDTO;
import com.homegravity.Odi.domain.party.dto.response.PartyResponseDTO;
import com.homegravity.Odi.domain.party.service.PartyService;
import com.homegravity.Odi.global.response.success.ApiResponse;
import com.homegravity.Odi.global.response.success.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "동승자 구인글 CRUD", description = "생성, 상세조회, 목록 조회(거리순, 출발시간 가까운 순)")
@Slf4j
@RestController
@RequestMapping("/api/party-boards")
@RequiredArgsConstructor
public class PartyController {

    private final PartyService partyService;

    @Operation(summary = "동승자 구인 글 작성", description = "동승자를 구하는 글을 작성합니다.")
    @PostMapping("")
    public ApiResponse<Long> createParty(@RequestBody PartyRequestDTO partyRequestDTO, @AuthenticationPrincipal Member member) {

        return ApiResponse.of(SuccessCode.PARTY_CREATE_SUCCESS, partyService.createParty(partyRequestDTO, member));
    }

    @Operation(summary = "동승자 구인 글 조회", description = "동승자를 구하는 글을 상세 조회합니다.")
    @GetMapping("/{party-id}")
    public ApiResponse<PartyResponseDTO> getPartyDetail(@PathVariable(value = "party-id") Long partyId, @AuthenticationPrincipal Member member) {

        return ApiResponse.of(SuccessCode.PARTY_GET_SUCCESS, partyService.getPartyDetail(partyId, member));
    }


    @Operation(summary = "동승자 구인 글 목록 조회", description = "필터링 기준, 정렬 기준에 따른 동승자 구인 글 목록을 조회합니다.")
    @GetMapping("")
    public ApiResponse<Slice<PartyDTO>> getAllParties(Pageable pageable, SelectPartyRequestDTO requestDTO) {
        log.info("조건 : {}, {}", pageable, requestDTO.toString());
        return ApiResponse.of(SuccessCode.PARTY_GET_SUCCESS, partyService.getAllParties(pageable, requestDTO));
    }

    @Operation(summary = "동승자 구인 글 수정", description = "동승자 구인 글을 수정합니다.")
    @PutMapping("/{party-id}")
    public ApiResponse<Long> updatePartyDetail(@PathVariable(value = "party-id") Long partyId, @RequestBody PartyRequestDTO partyRequestDTO, @AuthenticationPrincipal Member member) {
        return ApiResponse.of(SuccessCode.PARTY_UPDATE_SUCCESS, partyService.updateParty(partyId, partyRequestDTO, member));
    }


}
