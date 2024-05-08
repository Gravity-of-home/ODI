package com.homegravity.Odi.domain.chat.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class ChatRoomDTO implements Serializable {

    private static final long serialVersionUID = 6494678977089006639L;

    private String roomId;
    private List<ChatMessageDTO> chatMessages;
    private ChatMessageDTO lastMessage;

    public static ChatRoomDTO create() {
        ChatRoomDTO chatRoom = new ChatRoomDTO();
        chatRoom.roomId = UUID.randomUUID().toString();
        return chatRoom;
    }
}