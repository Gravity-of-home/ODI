package com.homegravity.Odi.domain.match.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.homegravity.Odi.domain.match.dto.MatchRequestDTO;
import com.homegravity.Odi.domain.match.service.MatchService;
import com.homegravity.Odi.domain.member.entity.Member;
import com.homegravity.Odi.domain.member.repository.MemberRepository;
import com.homegravity.Odi.global.response.error.ErrorCode;
import com.homegravity.Odi.global.response.error.exception.BusinessException;
import com.homegravity.Odi.global.response.success.ApiResponse;
import com.homegravity.Odi.global.response.success.SuccessCode;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Tag(name = "매칭", description = "조건이 같은 사용자끼리 매칭해줍니다.")
@Slf4j
@Controller
//@Validated
//@RequestMapping("/api/matches")
@RequiredArgsConstructor
public class MatchController {

    private final MatchService matchService;
    private final MemberRepository memberRepository;

    @MessageMapping("/match/{member-id}")
//    @SendTo("/topic/matchResult")
    public ApiResponse<Void> enterMatch(@DestinationVariable(value = "member-id") Long memberId, MatchRequestDTO matchRequestDTO) throws JsonProcessingException {

        Member requestMember = memberRepository.findById(memberId).orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_ID_NOT_EXIST, "없는 사용자입니다."));

        matchService.enterMatch(matchRequestDTO, requestMember);
        return ApiResponse.of(SuccessCode.MATCH_SUCCESS);
    }

//    @DeleteMapping("/{member-id}")
//    public ApiResponse<Void> cancelMatch(@RequestParam(value = "member-id") String memberId, @AuthenticationPrincipal Member member) {
//        matchService.cancelMatch(member);
//        return ApiResponse.of(SuccessCode.MATCH_SUCCESS);
//    }

}
