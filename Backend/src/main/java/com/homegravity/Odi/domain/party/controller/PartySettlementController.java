package com.homegravity.Odi.domain.party.controller;

import com.homegravity.Odi.domain.member.entity.Member;
import com.homegravity.Odi.domain.party.dto.response.PartySettlementResponseDto;
import com.homegravity.Odi.domain.party.service.PartySettlementService;
import com.homegravity.Odi.global.response.success.ApiResponse;
import com.homegravity.Odi.global.response.success.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "파티 정산", description = "파티 성사 이후와 관련된 API 리스트")
@RestController
@Validated
@RequestMapping("/api/parties")
@RequiredArgsConstructor
public class PartySettlementController {

    private final PartySettlementService partySettlementService;

    @Operation(summary = "동승(파티) 성사", description = "동승 파티 모집을 마감하고, 선불금액을 차감합니다.")
    @PostMapping("/{party-id}/success")
    public ApiResponse<PartySettlementResponseDto> matchParty(@AuthenticationPrincipal Member member, @PathVariable(name = "party-id") Long partyId) {

        return ApiResponse.of(SuccessCode.PARTY_MATCH_STATE_UPDATE_SUCCESS, partySettlementService.matchParty(member, partyId));
    }
}
