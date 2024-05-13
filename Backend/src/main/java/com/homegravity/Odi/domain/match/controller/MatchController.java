package com.homegravity.Odi.domain.match.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.homegravity.Odi.domain.match.dto.MatchRequestDTO;
import com.homegravity.Odi.domain.match.dto.MatchResponseDTO;
import com.homegravity.Odi.domain.match.service.MatchService;
import com.homegravity.Odi.domain.member.entity.Member;
import com.homegravity.Odi.domain.member.repository.MemberRepository;
import com.homegravity.Odi.global.response.error.ErrorCode;
import com.homegravity.Odi.global.response.error.exception.BusinessException;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "매칭", description = "조건이 같은 사용자끼리 매칭해줍니다.")
@Slf4j
@RestController
@RequiredArgsConstructor
public class MatchController {

    private final MatchService matchService;
    private final MemberRepository memberRepository;
    private final SimpMessageSendingOperations template;

    @MessageMapping("/match/{member-id}")
    public void enterMatch(@DestinationVariable(value = "member-id") Long memberId, MatchRequestDTO matchRequestDTO) throws JsonProcessingException {

        log.info("매칭 controller : {}", matchRequestDTO.toString());
        Member requestMember = memberRepository.findById(memberId).orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_ID_NOT_EXIST, "없는 사용자입니다."));

        MatchResponseDTO responseDTO = matchService.createMatch(matchRequestDTO, requestMember);

        if (responseDTO != null) {
            template.convertAndSend("/sub/matchResult/" + responseDTO.getMemberId1(), responseDTO);
            template.convertAndSend("/sub/matchResult/" + responseDTO.getMemberId2(), responseDTO);
        }

    }

//    @DeleteMapping("/{member-id}")
//    public ApiResponse<Void> cancelMatch(@RequestParam(value = "member-id") String memberId, @AuthenticationPrincipal Member member) {
//        matchService.cancelMatch(member);
//        return ApiResponse.of(SuccessCode.MATCH_SUCCESS);
//    }

}
