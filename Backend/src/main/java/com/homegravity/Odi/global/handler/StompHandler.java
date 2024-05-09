package com.homegravity.Odi.global.handler;

import com.homegravity.Odi.domain.chat.dto.ChatMessageDTO;
import com.homegravity.Odi.domain.chat.entity.MessageType;
import com.homegravity.Odi.domain.chat.repository.ChatRoomRepository;
import com.homegravity.Odi.domain.chat.service.ChatService;
import com.homegravity.Odi.domain.member.entity.Member;
import com.homegravity.Odi.domain.member.repository.MemberRepository;
import com.homegravity.Odi.global.jwt.util.JWTUtil;
import com.homegravity.Odi.global.response.error.ErrorCode;
import com.homegravity.Odi.global.response.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class StompHandler implements ChannelInterceptor {

    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;
    private final ChatService chatService;
    private final JWTUtil jwtUtil;

    // websocket을 통해 들어온 요청이 처리 되기전 실행된다.
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        String token = accessor.getFirstNativeHeader("token");
        log.info("방씀다 토큰임다 {}",token);
        Member sender = memberRepository.findById(Long.valueOf(jwtUtil.getId(token)))
                .orElseThrow(()-> new BusinessException(ErrorCode.MEMBER_ID_NOT_EXIST,ErrorCode.MEMBER_ID_NOT_EXIST.getMessage()));
        String nickname = sender.getNickname();
        String image = sender.getImage();
        if (StompCommand.CONNECT == accessor.getCommand()) { // websocket 연결요청
            log.info("!!!CONNECT!!! {}", sender.getNickname());
            // Header의 jwt token 검증
        } else if (StompCommand.SUBSCRIBE == accessor.getCommand()) { // 채팅룸 구독요청
            // header정보에서 구독 destination정보를 얻고, roomId를 추출한다.
            String roomId = chatService.getRoomId(Optional.ofNullable((String) message.getHeaders().get("simpDestination")).orElse("InvalidRoomId"));
            // 채팅방에 들어온 클라이언트 sessionId를 roomId와 맵핑해 놓는다.(나중에 특정 세션이 어떤 채팅방에 들어가 있는지 알기 위함)
            String sessionId = (String) message.getHeaders().get("simpSessionId");
            log.info("매핑이 되니????? {}",sessionId);
            chatRoomRepository.setUserEnterInfo(sessionId, roomId);
            // 클라이언트 입장 메시지를 채팅방에 발송한다.(redis publish)
            log.info("??!?!?!?!?!?!?!? {}", nickname);
            chatService.sendChatMessage(ChatMessageDTO.builder().roomId(roomId).senderNickname(nickname).senderImage(image).type(MessageType.ENTER).build());
            log.info("SUBSCRIBED {}, {}", nickname, roomId);
        } else if (StompCommand.DISCONNECT == accessor.getCommand()) { // Websocket 연결 종료
            // 연결이 종료된 클라이언트 sesssionId로 채팅방 id를 얻는다.
            String sessionId = (String) message.getHeaders().get("simpSessionId");
            String roomId = chatRoomRepository.getUserEnterRoomId(sessionId);
            // 클라이언트 퇴장 메시지를 채팅방에 발송한다.(redis publish)
            chatService.sendChatMessage(ChatMessageDTO.builder().roomId(roomId).senderNickname(nickname).senderImage(image).type(MessageType.QUIT).build());
            // 퇴장한 클라이언트의 roomId 맵핑 정보를 삭제한다.
            chatRoomRepository.removeUserEnterInfo(sessionId);
            log.info("DISCONNECTED {}, {}", sessionId, roomId);
        } else if (StompCommand.UNSUBSCRIBE == accessor.getCommand()) { // 채팅룸 구독취소
            log.info("!!!UNSUBSCRIBED!!!");
        }
        return message;
    }
}
