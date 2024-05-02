package com.homegravity.Odi.domain.party.controller;

import com.homegravity.Odi.domain.member.entity.Member;
import com.homegravity.Odi.domain.party.dto.request.PartyRequestDTO;
import com.homegravity.Odi.domain.party.dto.response.PartyResponseDTO;
import com.homegravity.Odi.domain.party.service.PartyService;
import com.homegravity.Odi.global.response.success.ApiResponse;
import com.homegravity.Odi.global.response.success.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

}
