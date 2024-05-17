package com.homegravity.Odi.domain.chat.controller;

import com.homegravity.Odi.domain.chat.dto.ChatDetailDTO;
import com.homegravity.Odi.domain.chat.dto.ChatListDTO;
import com.homegravity.Odi.domain.chat.repository.ChatRoomRepository;
import com.homegravity.Odi.domain.member.entity.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "채팅방 정보", description = "채팅방 목록 조회, 채팅방 상세 조회")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatRoomRepository chatRoomRepository;

    @Operation(summary = "채팅방 목록 조회", description = "내가 참여하고 있는 모든 채팅방 목록을 조회합니다.")
    @GetMapping("/rooms")
    @ResponseBody
    public List<ChatListDTO> room(@AuthenticationPrincipal Member member) {
        return chatRoomRepository.findAllRoomByMember(member);
    }

    @Operation(summary = "채팅방 상세 조회", description = "채팅방 ID에 해당하는 채팅방 상세 정보를 조회합니다.")
    @GetMapping("/room/{room-id}")
    @ResponseBody
    public ChatDetailDTO roomInfo(@PathVariable(value = "room-id") String roomId, Pageable pageable) {
        return chatRoomRepository.findRoomById(roomId, pageable);
    }

}
