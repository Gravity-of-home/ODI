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
