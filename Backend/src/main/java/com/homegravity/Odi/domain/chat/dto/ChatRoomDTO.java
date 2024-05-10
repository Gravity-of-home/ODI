package com.homegravity.Odi.domain.chat.dto;

import com.homegravity.Odi.domain.party.entity.Party;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
public class ChatRoomDTO implements Serializable {

    private static final long serialVersionUID = 6494678977089006639L;

    private String roomId;
    private List<ChatMessageDTO> chatMessages;
    private ChatMessageDTO lastMessage;

    public static ChatRoomDTO create() {
        ChatRoomDTO chatRoom = ChatRoomDTO.builder().build();
        chatRoom.roomId = UUID.randomUUID().toString();
        return chatRoom;
    }

    @Builder
    public ChatRoomDTO (String roomId, List<ChatMessageDTO> chatMessages, ChatMessageDTO lastMessage) {
        this.roomId = roomId;
        this.chatMessages = chatMessages;
        this.lastMessage = lastMessage;
    }
}