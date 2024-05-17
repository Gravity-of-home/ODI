package com.homegravity.Odi.global.pubsub;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.homegravity.Odi.domain.chat.dto.ChatMessageDTO;
import com.homegravity.Odi.domain.notification.dto.NotificationDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisSubscriber {

    private final ObjectMapper objectMapper;
    private final SimpMessageSendingOperations messagingTemplate;

    /**
     * Redis에서 메시지가 발행(publish)되면 대기하고 있던 Redis Subscriber가 해당 메시지를 받아 처리한다.
     */
    public void sendMessage(String publishMessage) {
        try {
            // ChatMessage 객채로 맵핑
            ChatMessageDTO chatMessage = objectMapper.readValue(publishMessage, ChatMessageDTO.class);
            // 채팅방을 구독한 클라이언트에게 메시지 발송
            messagingTemplate.convertAndSend("/sub/chat/room/" + chatMessage.getRoomId(), chatMessage);
        } catch (Exception e) {
            log.error("메세지 처리 중 에러 발생: Exception {}", e);
        }
    }

    public void sendNotification(String publishNotification) {
        try {
            // Notification 객채로 맵핑
            NotificationDTO notification = objectMapper.readValue(publishNotification, NotificationDTO.class);
            // 알림을 구독한 클라이언트에게 메시지 발송
            messagingTemplate.convertAndSend("/sub/notification/" + notification.getReceiverId(), notification);
        } catch (Exception e) {
            log.error("메세지 처리 중 에러 발생: Exception {}", e);
        }
    }
}