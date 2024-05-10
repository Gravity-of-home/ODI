package com.homegravity.Odi.domain.chat.controller;

import com.homegravity.Odi.domain.chat.dto.ChatRoomDTO;
import com.homegravity.Odi.domain.chat.repository.ChatRoomRepository;
import com.homegravity.Odi.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/api/chat")
public class ChatRoomController {

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
