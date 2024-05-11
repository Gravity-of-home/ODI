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
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "매칭", description = "조건이 같은 사용자끼리 매칭해줍니다.")
@Slf4j
@Controller
//@Validated
//@RequestMapping("/api/matches")
@RequiredArgsConstructor
public class MatchController {

    private final MatchService matchService;
    private final MemberRepository memberRepository;

    @MessageMapping("/match/{member-id}/enter")
//    @SendTo("/topic/matchResult")
    public ApiResponse<Void> enterMatch(@DestinationVariable(value = "member-id") Long memberId, @RequestBody MatchRequestDTO matchRequestDTO) throws JsonProcessingException {

        log.warn("소켓 연결? {}", memberId);

        Member requestMember = memberRepository.findById(memberId).orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_ID_NOT_EXIST, "없음요"));

        matchService.enterMatch(matchRequestDTO, requestMember);
        return ApiResponse.of(SuccessCode.MATCH_SUCCESS);
    }

//    @DeleteMapping("/{member-id}")
//    public ApiResponse<Void> cancelMatch(@RequestParam(value = "member-id") String memberId, @AuthenticationPrincipal Member member) {
//        matchService.cancelMatch(member);
//        return ApiResponse.of(SuccessCode.MATCH_SUCCESS);
//    }

}
