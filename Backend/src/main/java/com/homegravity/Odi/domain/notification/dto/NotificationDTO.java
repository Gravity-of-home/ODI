package com.homegravity.Odi.domain.notification.dto;

import com.homegravity.Odi.domain.notification.entity.Notification;
import com.homegravity.Odi.domain.notification.entity.NotificationType;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationDTO {

    @NotNull
    private Long receiverId; // 알림받는 사람 아이디

    @NotNull
    private Long partyId; // 관련 동승글 아이디

    @NotNull
    private String content; // 알림 내용

    private String sendTime; // 알림 보낸 시간

    @NotNull
    private NotificationType type; // 알림 타입

    @Builder
    public NotificationDTO (Long receiverId, Long partyId, String content, String sendTime, NotificationType type) {
        this.receiverId = receiverId;
        this.partyId = partyId;
        this.content = content;
        this.sendTime = sendTime;
        this.type = type;
    }

    public static NotificationDTO from (Notification notification) {
        return builder()
                .receiverId(notification.getReceiver().getId())
                .partyId(notification.getParty().getId())
                .content(notification.getContent())
                .sendTime(notification.getCreatedAt().toString())
                .type(notification.getNotificationType())
                .build();
    }

}
