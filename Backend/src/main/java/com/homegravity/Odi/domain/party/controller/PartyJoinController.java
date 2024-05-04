package com.homegravity.Odi.domain.party.controller;

import com.homegravity.Odi.domain.member.entity.Member;
import com.homegravity.Odi.domain.party.service.PartyService;
import com.homegravity.Odi.global.response.success.ApiResponse;
import com.homegravity.Odi.global.response.success.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "동승 구인 참여 신청", description = "동승 구인 파티 참여 신청/취소/수락/거절 합니다.")
@RestController
@RequestMapping("/api/parties")
@RequiredArgsConstructor
public class PartyJoinController {
    private final PartyService partyService;

    @Operation(summary = "동승자 구인 참여 신청", description = "동승자 구인 파티에 참여 신청을 합니다.")
    @PostMapping("/{party-id}")
    public ApiResponse<Long> joinParty(@PathVariable(value = "party-id") Long partyId, @AuthenticationPrincipal Member member) {
        return ApiResponse.of(SuccessCode.PARTY_SUBSCRIBE_SUCCESS, partyService.joinParty(partyId, member));
    }
}
