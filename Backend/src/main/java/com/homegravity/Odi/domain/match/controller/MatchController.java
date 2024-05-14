package com.homegravity.Odi.domain.match.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.homegravity.Odi.domain.match.dto.MatchRequestDTO;
import com.homegravity.Odi.domain.match.dto.MatchResponseDTO;
import com.homegravity.Odi.domain.match.service.MatchService;
import com.homegravity.Odi.global.response.success.ApiResponse;
import com.homegravity.Odi.global.response.success.SuccessCode;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @DeleteMapping("/api/matches/{member-id}")
    public ApiResponse<Void> cancelMatch(@PathVariable(value = "member-id") Long memberId) {
        log.info("삭제 api 요청 : {}", memberId);
        matchService.cancelMatch(memberId);
        return ApiResponse.of(SuccessCode.MATCH_CANCEL_SUCCESS);
    }

}
