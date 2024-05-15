package com.homegravity.Odi.domain.chat.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class ChatDetailDTO implements Serializable {

    private static final long serialVersionUID = 6494678977089006639L;

    private String roomId;
    private String partyTitle;
    private List<ChatMessageDTO> chatMessages;

    public static ChatDetailDTO create() {
        ChatDetailDTO chatRoom = ChatDetailDTO.builder().build();
        chatRoom.roomId = UUID.randomUUID().toString();
        return chatRoom;
    }

    @Builder
    public ChatDetailDTO(String roomId, String partyTitle, List<ChatMessageDTO> chatMessages) {
        this.roomId = roomId;
        this.partyTitle = partyTitle;
        this.chatMessages = chatMessages;
    }
}