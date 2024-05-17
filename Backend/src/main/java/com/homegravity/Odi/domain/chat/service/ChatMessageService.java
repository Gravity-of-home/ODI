package com.homegravity.Odi.domain.chat.service;

import com.homegravity.Odi.domain.chat.dto.ChatMessageDTO;
import com.homegravity.Odi.domain.chat.entity.ChatMessage;
import com.homegravity.Odi.domain.chat.repository.ChatMessageRepository;
import com.homegravity.Odi.domain.party.entity.Party;
import com.homegravity.Odi.domain.party.respository.PartyRepository;
import com.homegravity.Odi.global.response.error.ErrorCode;
import com.homegravity.Odi.global.response.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final PartyRepository partyRepository;

    /**
     * 특정 채팅방의 채팅 메세지 페이지네이션 조회
     */
    public List<ChatMessageDTO> getSliceChatMessage(String roomId, Pageable pageable) {
        // 채팅방 ID 기반 Party 조회
        Party party = partyRepository.findByRoomIdAndDeletedAtIsNull(roomId)
                .orElseThrow(()->new BusinessException(ErrorCode.NOT_FOUND_ERROR,ErrorCode.NOT_FOUND_ERROR.getMessage()));
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        Slice<ChatMessage> chatMessages = chatMessageRepository.findAllByPartyId(party.getId(), pageRequest)
                .orElseThrow(()->new BusinessException(ErrorCode.NOT_FOUND_ERROR,ErrorCode.NOT_FOUND_ERROR.getMessage()));
        return chatMessages.stream().map(ChatMessageDTO::from).collect(Collectors.toList()).reversed();
    }

    /**
     * 특정 채팅방 마지막 채팅 메세지 조회
     */
    public ChatMessageDTO getLastMessage(String roomId) {
        // 채팅방 ID 기반 Party 조회
        Party party = partyRepository.findByRoomIdAndDeletedAtIsNull(roomId)
                .orElseThrow(()->new BusinessException(ErrorCode.NOT_FOUND_ERROR,ErrorCode.NOT_FOUND_ERROR.getMessage()));
        return chatMessageRepository.findTopByIdOrderByCreatedAtDesc(party.getId())
                .map(ChatMessageDTO::from)
                .orElse(null);
    }
}
