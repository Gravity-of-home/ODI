package com.homegravity.Odi.domain.chat.service;

import com.homegravity.Odi.domain.chat.dto.ChatMessageDTO;
import com.homegravity.Odi.domain.chat.entity.ChatMessage;
import com.homegravity.Odi.domain.chat.entity.MessageType;
import com.homegravity.Odi.domain.chat.repository.ChatMessageRepository;
import com.homegravity.Odi.domain.member.entity.Member;
import com.homegravity.Odi.domain.member.repository.MemberRepository;
import com.homegravity.Odi.domain.party.entity.Party;
import com.homegravity.Odi.domain.party.respository.PartyRepository;
import com.homegravity.Odi.global.response.error.ErrorCode;
import com.homegravity.Odi.global.response.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatService {

    private final ChatMessageRepository chatMessageRepository;
    private final MemberRepository memberRepository;
    private final PartyRepository partyRepository;

    private final ChannelTopic channelTopic;
    private final RedisTemplate redisTemplate;

    /**
     * destination정보에서 roomId 추출
     */
    public String getRoomId(String destination) {
        int lastIndex = destination.lastIndexOf('/');
        if (lastIndex != -1)
            return destination.substring(lastIndex + 1);
        else
            return "";
    }

    /**
     * 채팅방에 메시지 발송
     */
    public void sendChatMessage(ChatMessageDTO chatMessage) {
        if (MessageType.ENTER.equals(chatMessage.getType())) {
            chatMessage.setContent(chatMessage.getSenderNickname() + "님이 방에 입장했습니다.");
        } else if (MessageType.QUIT.equals(chatMessage.getType())) {
            chatMessage.setContent(chatMessage.getSenderNickname() + "님이 방에서 나갔습니다.");
        } else if (MessageType.DATE.equals(chatMessage.getType())) {
            chatMessage.setContent(chatMessage.getSendTime().toString());
        } else if (MessageType.SETTLEMENT.equals(chatMessage.getType())) {
            chatMessage.setContent(chatMessage.getSenderNickname() + "님이 정산을 요청하셨습니다.");
        }
        log.info("시간어떻게들어오니??? {}",chatMessage.getSendTime());
        redisTemplate.convertAndSend(channelTopic.getTopic(), chatMessage);
        log.info("@@@@@@@@@@@@@@@@@@ {}",chatMessage.getRoomId());
        // 메세지 닉네임 기반으로 유저 조회
        Member sender = memberRepository.findByNicknameAndDeletedAtIsNull(chatMessage.getSenderNickname())
                .orElseThrow(()->new BusinessException(ErrorCode.MEMBER_NICKNAME_NOT_EXIST,ErrorCode.MEMBER_NICKNAME_NOT_EXIST.getMessage()));
        Party party = partyRepository.findByRoomIdAndDeletedAtIsNull(chatMessage.getRoomId())
                .orElseThrow(()->new BusinessException(ErrorCode.NOT_FOUND_ERROR, ErrorCode.NOT_FOUND_ERROR.getMessage()));
        // 메세지 DB 저장
        chatMessageRepository.save(ChatMessage.builder()
                        .content(chatMessage.getContent())
                        .sender(sender)
                        .party(party)
                        .messageType(chatMessage.getType())
                .build());
        log.info("저장저장저장저장저장저장");
    }
}
