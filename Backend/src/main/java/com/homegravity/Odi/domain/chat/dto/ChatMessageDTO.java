package com.homegravity.Odi.domain.chat.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    private String sender; // 메시지 보낸사람

    @NotNull
    private String content; // 메시지 내용

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime sendTime; // 메세지 보낸 시간

    @NotNull
    private MessageType type; // 메시지 타입

    @NotNull
    private String roomId;

    @Builder
    public ChatMessageDTO(String sender, String content, LocalDateTime sendTime, MessageType type, String roomId) {
        this.sender = sender;
        this.content = content;
        this.sendTime = sendTime;
        this.type = type;
        this.roomId = roomId;
    }

}
