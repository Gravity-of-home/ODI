package com.homegravity.Odi.domain.chat.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatListDTO {

    private String roomId;
    private ChatMessageDTO lastMessage;

    @Builder
    public ChatListDTO(String roomId, ChatMessageDTO lastMessage) {
        this.roomId = roomId;
        this.lastMessage = lastMessage;
    }

}
