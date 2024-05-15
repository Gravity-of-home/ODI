package com.homegravity.Odi.domain.chat.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatListDTO {

    private Long partyId;
    private String roomId;
    private String partyTitle;
    private ChatMessageDTO lastMessage;

    @Builder
    public ChatListDTO(Long partyId, String roomId, String partyTitle, ChatMessageDTO lastMessage) {
        this.partyId = partyId;
        this.roomId = roomId;
        this.partyTitle = partyTitle;
        this.lastMessage = lastMessage;
    }

}
