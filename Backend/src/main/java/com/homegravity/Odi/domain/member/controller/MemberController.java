package com.homegravity.Odi.domain.member.controller;

import com.homegravity.Odi.domain.member.dto.request.MemberUpdateRequestDTO;
import com.homegravity.Odi.domain.member.dto.response.MemberResponseDTO;
import com.homegravity.Odi.domain.member.entity.Member;
import com.homegravity.Odi.domain.member.service.MemberService;
import com.homegravity.Odi.global.response.success.ApiResponse;
import com.homegravity.Odi.global.response.success.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "사용자 정보", description = "사용자 상세정보 조회/수정")
@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @Operation(summary = "사용자 정보 조회", description = "사용자의 상세정보를 조회합니다.")
    @GetMapping("/me")
    public ApiResponse<MemberResponseDTO> getMemberInfo(@AuthenticationPrincipal Member member){
        return ApiResponse.of(SuccessCode.MEMBER_GET_SUCCESS, memberService.getMemberInfo(member));
    }

    @Operation(summary = "회원정보 수정", description = "회원 정보(닉네임, 사진) 변경합니다. 닉네임 변경의 경우 중복되지 않는 닉네임으로 변경 가능하도록 합니다.")
    @PutMapping("/me")
    public ApiResponse<MemberResponseDTO>updateMemberInfo(MemberUpdateRequestDTO memberUpdateRequestDTO, @AuthenticationPrincipal Member member){
        return ApiResponse.of(SuccessCode.MEMBER_UPDATE_SUCCESS, memberService.updateMemberInfo(memberUpdateRequestDTO, member));
    }
}
