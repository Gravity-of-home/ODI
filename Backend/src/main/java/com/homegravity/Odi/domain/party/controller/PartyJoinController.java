package com.homegravity.Odi.domain.party.controller;

import com.homegravity.Odi.domain.member.entity.Member;
import com.homegravity.Odi.domain.party.service.PartyService;
import com.homegravity.Odi.global.response.success.ApiResponse;
import com.homegravity.Odi.global.response.success.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "동승 구인 참여 신청", description = "동승 구인 파티 참여 신청/취소/수락/거절 합니다.")
@RestController
@RequestMapping("/api/parties/{party-id}")
@RequiredArgsConstructor
public class PartyJoinController {
    private final PartyService partyService;

    @Operation(summary = "동승자 구인 참여 신청", description = "동승자 구인 파티에 참여 신청을 합니다.")
    @PostMapping("")
    public ApiResponse<Long> joinParty(@PathVariable(value = "party-id") Long partyId, @AuthenticationPrincipal Member member) {
        return ApiResponse.of(SuccessCode.PARTY_SUBSCRIBE_SUCCESS, partyService.joinParty(partyId, member));
    }

    @Operation(summary = "동승자 구인 파티 참여 신청 취소", description = "동승자 구인 파티 참여에 신청한것 및 참여에 대해 취소를 합니다.")
    @DeleteMapping("")
    public ApiResponse<Boolean> deleteJoinParty(@PathVariable(value = "party-id") Long partyId, @AuthenticationPrincipal Member member) {
        return ApiResponse.of(SuccessCode.PARTY_DELETE_SUBSCRIBE_SUCCESS, partyService.deleteJoinParty(partyId, member));
    }

    @Operation(summary = "동승자 구인 파티 신청자 수락하기 (방장만)", description = "방장이 동승자 구인 파티 신청자를 수락합니다.")
    @PutMapping("/{member-id}")
    public ApiResponse<Boolean> acceptJoinParty(@PathVariable(value = "party-id") Long partyId, @PathVariable(value = "member-id") Long memberId, @AuthenticationPrincipal Member member) {
        return ApiResponse.of(SuccessCode.PARTY_ACCEPT_SUBSCRIBE_SUCCESS, partyService.acceptJoinParty(partyId, memberId, member));
    }

    @Operation(summary = "동승자 구인 파티 신청 거절", description = "방장이 동승자 구인 파티 신청자를 거절합니다.")
    @DeleteMapping("/{member-id}")
    public ApiResponse<String> refuseJoinParty(@PathVariable(value = "party-id") Long partyId, @PathVariable(value = "member-id") Long memberId, @AuthenticationPrincipal Member member){
        return ApiResponse.of(SuccessCode.PARTY_REFUSE_SUCCESS, partyService.refuseJoinParty(partyId, memberId, member));
    }
}
