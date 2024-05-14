package com.homegravity.Odi.global.pubsub;

import com.homegravity.Odi.domain.chat.dto.ChatMessageDTO;
import com.homegravity.Odi.domain.chat.service.ChatService;
import com.homegravity.Odi.domain.member.entity.Member;
import com.homegravity.Odi.domain.member.repository.MemberRepository;
import com.homegravity.Odi.domain.notification.dto.NotificationDTO;
import com.homegravity.Odi.domain.notification.service.NotificationService;
import com.homegravity.Odi.global.jwt.util.JWTUtil;
import com.homegravity.Odi.global.response.error.ErrorCode;
import com.homegravity.Odi.global.response.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@RequiredArgsConstructor
@RestController
public class StompController {

    private final MemberRepository memberRepository;
    private final ChatService chatService;
    private final NotificationService notificationService;

    private final JWTUtil jwtUtil;

    /**
     * websocket "/pub/chat/message"로 들어오는 메시징을 처리한다.
     */
    @MessageMapping("/chat/message")
    public void message(ChatMessageDTO message, @Header("token") String token) {
        Member sender = memberRepository.findById(Long.valueOf(jwtUtil.getId(token)))
                .orElseThrow(()-> new BusinessException(ErrorCode.MEMBER_ID_NOT_EXIST,ErrorCode.MEMBER_ID_NOT_EXIST.getMessage()));
        log.info("{}", sender.getNickname());
        String nickname = sender.getNickname();
        String image = sender.getImage();
        // 로그인 회원 정보로 대화명 설정
        message.setSenderNickname(nickname);
        message.setSenderImage(image);
        message.setSendTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        // Websocket에 발행된 메시지를 redis로 발행(publish)
        log.info("채팅pub!!!!!!!!!! {}", message.getSendTime());
        chatService.sendChatMessage(message);
    }

    /**
     * websocket "/pub/notification/{receiver-id}"로 들어오는 메시징을 처리한다.
     */
    @MessageMapping("/notification/{receiver-id}")
    public void message(@PathVariable(value = "receiver-id") Long receiverId, NotificationDTO message) {
        Member receiver = memberRepository.findById(receiverId)
                .orElseThrow(()-> new BusinessException(ErrorCode.MEMBER_ID_NOT_EXIST,ErrorCode.MEMBER_ID_NOT_EXIST.getMessage()));
        log.info("{}", receiver.getNickname());
        // 로그인 회원 정보로 대화명 설정
        message.setReceiverId(receiverId);
        message.setSendTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        // Websocket에 발행된 메시지를 redis로 발행(publish)
        log.info("알림pub!!!!!!!!!! {}", message.getSendTime());
        notificationService.sendNotification(message);
    }
}