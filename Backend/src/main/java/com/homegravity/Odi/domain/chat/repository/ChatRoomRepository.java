package com.homegravity.Odi.domain.chat.repository;

import com.homegravity.Odi.domain.chat.dto.ChatDetailDTO;
import com.homegravity.Odi.domain.chat.dto.ChatListDTO;
import com.homegravity.Odi.domain.chat.dto.ChatMessageDTO;
import com.homegravity.Odi.domain.chat.service.ChatMessageService;
import com.homegravity.Odi.domain.member.entity.Member;
import com.homegravity.Odi.domain.party.entity.Party;
import com.homegravity.Odi.domain.party.entity.PartyMember;
import com.homegravity.Odi.domain.party.respository.PartyMemberRepository;
import com.homegravity.Odi.domain.party.respository.PartyRepository;
import com.homegravity.Odi.domain.party.service.PartyService;
import com.homegravity.Odi.global.response.error.ErrorCode;
import com.homegravity.Odi.global.response.error.exception.BusinessException;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ChatRoomRepository {
    // Redis CacheKeys
    private static final String CHAT_ROOMS = "CHAT_ROOM"; // 채팅룸 저장
//    public static final String ENTER_INFO = "ENTER_INFO"; // 채팅룸에 입장한 클라이언트의 sessionId와 채팅룸 id를 맵핑한 정보 저장

    private final ChatMessageService chatMessageService;
    private final PartyRepository partyRepository;
    private final PartyMemberRepository partyMemberRepository;

    @Resource(name = "redisTemplate")
    private HashOperations<String, String, ChatDetailDTO> hashOpsChatRoom;
//    @Resource(name = "redisTemplate")
//    private HashOperations<String, String, String> hashOpsEnterInfo;

    // 모든 채팅방 목록 조회
    public List<ChatListDTO> findAllRoomByMember(Member member) {
//        return hashOpsChatRoom.values(CHAT_ROOMS);
        // 유저 기반으로 참여하고 있는 파티 모두 조회
        List<Party> parties = partyMemberRepository.findAllByMember(member).stream()
                .map(PartyMember::getParty)
                .toList();
        // 채팅방 ID로 채팅방 목록 모두 조회
        return parties.stream()
                .map(party -> ChatListDTO.builder()
                        .roomId(party.getRoomId())
                        .partyTitle(party.getTitle())
                        .lastMessage(chatMessageService.getLastMessage(party.getRoomId()))
                        .build())
                .toList();
    }

    // 특정 채팅방 조회
    public ChatDetailDTO findRoomById(String roomId) {
//        ChatDetailDTO chatRoom = hashOpsChatRoom.get(CHAT_ROOMS, id);
//        assert chatRoom != null;
        Party party = partyRepository.findByRoomIdAndDeletedAtIsNull(roomId)
                .orElseThrow(()->new BusinessException(ErrorCode.NOT_FOUND_ERROR,ErrorCode.NOT_FOUND_ERROR.getMessage()));
        return ChatDetailDTO.builder()
                .roomId(roomId)
                .partyTitle(party.getTitle())
                .chatMessages(chatMessageService.getAllChatMessage(roomId))
                .build();
    }

    // 채팅방 생성 : 서버간 채팅방 공유를 위해 redis hash에 저장한다.
    public String createChatRoom() {
        ChatDetailDTO chatRoom = ChatDetailDTO.create();
        hashOpsChatRoom.put(CHAT_ROOMS, chatRoom.getRoomId(), chatRoom);
        return chatRoom.getRoomId();
    }

//    // 유저가 입장한 채팅방ID와 유저 세션ID 맵핑 정보 저장
//    public void setUserEnterInfo(String sessionId, String roomId) {
//        hashOpsEnterInfo.put(ENTER_INFO, sessionId, roomId);
//    }
//
//    // 유저 세션으로 입장해 있는 채팅방 ID 조회
//    public String getUserEnterRoomId(String sessionId) {
//        return hashOpsEnterInfo.get(ENTER_INFO, sessionId);
//    }
//
//    // 유저 세션정보와 맵핑된 채팅방ID 삭제
//    public void removeUserEnterInfo(String sessionId) {
//        hashOpsEnterInfo.delete(ENTER_INFO, sessionId);
//    }

}