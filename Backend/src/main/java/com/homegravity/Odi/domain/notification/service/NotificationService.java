package com.homegravity.Odi.domain.notification.service;

import com.homegravity.Odi.domain.member.entity.Member;
import com.homegravity.Odi.domain.member.repository.MemberRepository;
import com.homegravity.Odi.domain.notification.dto.NotificationDTO;
import com.homegravity.Odi.domain.notification.entity.Notification;
import com.homegravity.Odi.domain.notification.entity.NotificationType;
import com.homegravity.Odi.domain.notification.repository.NotificationRepository;
import com.homegravity.Odi.domain.party.entity.Party;
import com.homegravity.Odi.domain.party.respository.PartyRepository;
import com.homegravity.Odi.global.response.error.ErrorCode;
import com.homegravity.Odi.global.response.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final MemberRepository memberRepository;
    private final PartyRepository partyRepository;

    private final ChannelTopic notificationTopic;
    private final RedisTemplate redisTemplate;

    /**
     * 개인 알림방에 메시지 발송
     */
    public void sendNotification(NotificationDTO notification) {
        // 메세지 ID 기반으로 유저 조회
        Member receiver = memberRepository.findByIdAndDeletedAtIsNull(notification.getReceiverId())
                .orElseThrow(()->new BusinessException(ErrorCode.MEMBER_ID_NOT_EXIST,ErrorCode.MEMBER_ID_NOT_EXIST.getMessage()));
        Party party = partyRepository.findById(notification.getPartyId())
                .orElseThrow(()->new BusinessException(ErrorCode.NOT_FOUND_ERROR, ErrorCode.NOT_FOUND_ERROR.getMessage()));

        switch (notification.getType()) {
            case NotificationType.APPLY:
                notification.setContent(party.getTitle()+" 파티에 동승 신청이 들어왔어요!"); break;
            case NotificationType.ACCEPT:
                notification.setContent(party.getTitle()+" 파티에 참가했어요!"); break;
            case NotificationType.REJECT:
                notification.setContent(party.getTitle()+" 파티에 거절됐습니다."); break;
            case NotificationType.KICK:
                notification.setContent(party.getTitle()+" 파티에 강퇴되었습니다."); break;
            case NotificationType.QUIT:
                notification.setContent(party.getTitle()+" 파티의 " + notification.getSenderNickname() + " 님이 퇴장하셨습니다."); break;
            case NotificationType.SETTLEMENT:
                notification.setContent(party.getTitle()+" 파티의 " + notification.getSenderNickname() + " 님이 정산을 요청하셨습니다."); break;
        }
        log.info("시간어떻게들어오니??? {}",notification.getSendTime());
        redisTemplate.convertAndSend(notificationTopic.getTopic(), notification);
        log.info("@@@@@@@@@@@@@@@@@@ {}",notification.getReceiverId());

        // 알림 DB 저장
        notificationRepository.save(Notification.builder()
                .content(notification.getContent())
                .receiver(receiver)
                .party(party)
                .notificationType(notification.getType())
                .build());
        log.info("알림저장저장저장저장저장");
    }

    // 특정 유저의 모든 알림 조회
    public List<NotificationDTO> getAllNotificationsByMember(Member member) {
        return notificationRepository.findAllByReceiverIdAndDeletedAtIsNull(member.getId())
                .orElseThrow(()->new BusinessException(ErrorCode.NOT_FOUND_ERROR,ErrorCode.NOT_FOUND_ERROR.getMessage()))
                .stream()
                .map(NotificationDTO::from)
                .collect(Collectors.toList());
    }

    // 특정 알림 읽음 처리
    @Transactional
    public NotificationDTO updateNotification(Long notificationId) {
        Notification notification = notificationRepository.findByIdAndDeletedAtIsNull(notificationId)
                .orElseThrow(()-> new BusinessException(ErrorCode.NOT_FOUND_ERROR,ErrorCode.NOT_FOUND_ERROR.getMessage()));
        notification.updateIsRead();
        return NotificationDTO.from(notification);
    }

    // 알림 삭제 처리
    @Transactional
    public void deleteNotification(Long notificationId) {
        Notification notification = notificationRepository.findByIdAndDeletedAtIsNull(notificationId)
                .orElseThrow(()-> new BusinessException(ErrorCode.NOT_FOUND_ERROR,ErrorCode.NOT_FOUND_ERROR.getMessage()));
        notificationRepository.delete(notification);
    }
}
