package com.homegravity.Odi.domain.chat.controller;

import com.homegravity.Odi.domain.chat.dto.ChatRoomDTO;
import com.homegravity.Odi.domain.chat.repository.ChatRoomRepository;
import com.homegravity.Odi.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatRoomRepository chatRoomRepository;

    // 모든 채팅방 목록 반환
    @GetMapping("/rooms")
    @ResponseBody
    public List<ChatRoomDTO> room(@AuthenticationPrincipal Member member) {
        return chatRoomRepository.findAllRoomByMember(member);
    }

    // 특정 채팅방 조회
    @GetMapping("/room/{room-id}")
    @ResponseBody
    public ChatRoomDTO roomInfo(@PathVariable(value = "room-id") String roomId) {
        return chatRoomRepository.findRoomById(roomId);
    }

}
