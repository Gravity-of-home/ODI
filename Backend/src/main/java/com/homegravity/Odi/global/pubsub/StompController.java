package com.homegravity.Odi.global.pubsub;

import com.homegravity.Odi.domain.chat.dto.ChatMessageDTO;
import com.homegravity.Odi.domain.member.entity.Member;
import com.homegravity.Odi.domain.member.repository.MemberRepository;
import com.homegravity.Odi.domain.notification.dto.NotificationDTO;
import com.homegravity.Odi.global.jwt.util.JWTUtil;
import com.homegravity.Odi.global.response.error.ErrorCode;
import com.homegravity.Odi.global.response.error.exception.BusinessException;
import com.homegravity.Odi.global.service.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Slf4j
@RequiredArgsConstructor
@RestController
public class StompController {

    private final MemberRepository memberRepository;
    private final RedisService redisService;

    private final JWTUtil jwtUtil;

    /**
     * websocket "/pub/chat/message"로 들어오는 메시징을 처리한다.
     */
    @MessageMapping("/chat/message")
    public void message(ChatMessageDTO message, @Header("token") String token) {
        Member sender = memberRepository.findById(Long.valueOf(jwtUtil.getId(token)))
                .orElseThrow(()-> new BusinessException(ErrorCode.MEMBER_ID_NOT_EXIST,ErrorCode.MEMBER_ID_NOT_EXIST.getMessage()));
        String nickname = sender.getNickname();
        String image = sender.getImage();
        // 로그인 회원 정보로 대화명 설정
        message.setSenderNickname(nickname);
        message.setSenderImage(image);
        message.setSendTime(LocalDateTime.now(ZoneId.of("Asia/Seoul")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        // Websocket에 발행된 메시지를 redis로 발행(publish)
        redisService.sendChatMessage(message);
    }

    /**
     * websocket "/pub/notification/{receiver-id}"로 들어오는 메시징을 처리한다.
     */
    @MessageMapping("/notification/{receiver-id}")
    public void message(@DestinationVariable(value = "receiver-id") Long receiverId, NotificationDTO message, @Header("token") String token) {
        Member sender = memberRepository.findById(Long.valueOf(jwtUtil.getId(token)))
                .orElseThrow(()-> new BusinessException(ErrorCode.MEMBER_ID_NOT_EXIST,ErrorCode.MEMBER_ID_NOT_EXIST.getMessage()));
        // 알림 받는 사람 설정
        message.setReceiverId(receiverId);
        // 알림 보낸 사람 설정
        message.setSenderNickname(sender.getNickname());
        message.setSendTime(LocalDateTime.now(ZoneId.of("Asia/Seoul")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        // Websocket에 발행된 메시지를 redis로 발행(publish)
        redisService.sendNotification(message);
    }
}