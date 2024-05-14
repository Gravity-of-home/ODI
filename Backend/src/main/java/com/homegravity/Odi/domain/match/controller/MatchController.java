package com.homegravity.Odi.domain.match.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.homegravity.Odi.domain.match.dto.MatchRequestDTO;
import com.homegravity.Odi.domain.match.dto.MatchResponseDTO;
import com.homegravity.Odi.domain.match.service.MatchService;
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
    private final SimpMessageSendingOperations template;

    @MessageMapping("/match/{member-id}")
    public void enterMatch(@DestinationVariable(value = "member-id") Long memberId, MatchRequestDTO matchRequestDTO) throws JsonProcessingException {

        MatchResponseDTO responseDTO = matchService.createMatch(matchRequestDTO, memberId);

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
