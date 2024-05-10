package com.homegravity.Odi.domain.chat.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.homegravity.Odi.domain.chat.entity.ChatMessage;
import com.homegravity.Odi.domain.chat.entity.MessageType;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ChatMessageDTO {

    @NotNull
    private Long partyId; // 동승글 아이디

    @NotNull
    private String roomId; // 채팅방 아이디

    @NotNull
    private String senderImage; // 메세지 보낸 사람 프로필사진

    @NotNull
    private String senderNickname; // 메시지 보낸 사람 이름

    @NotNull
    private String content; // 메시지 내용

    private String sendTime; // 메세지 보낸 시간

    @NotNull
    private MessageType type; // 메시지 타입

    @Builder
    public ChatMessageDTO(Long partyId, String roomId, String senderImage, String senderNickname, String content, String sendTime, MessageType type) {
        this.partyId=partyId;
        this.roomId=roomId;
        this.senderImage = senderImage;
        this.senderNickname = senderNickname;
        this.content = content;
        this.sendTime = sendTime;
        this.type = type;
    }

    public static ChatMessageDTO from (ChatMessage chatMessage) {
        return builder()
                .partyId(chatMessage.getParty().getId())
                .roomId(chatMessage.getParty().getRoomId())
                .senderImage(chatMessage.getSender().getImage())
                .senderNickname(chatMessage.getSender().getNickname())
                .content(chatMessage.getContent())
                .sendTime(chatMessage.getCreatedAt().toString())
                .type(chatMessage.getMessageType())
                .build();
    }
}
