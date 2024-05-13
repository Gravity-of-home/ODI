package com.homegravity.Odi.domain.notification.controller;


import com.homegravity.Odi.domain.member.entity.Member;
import com.homegravity.Odi.domain.notification.dto.NotificationDTO;
import com.homegravity.Odi.domain.notification.service.NotificationService;
import com.homegravity.Odi.global.response.success.ApiResponse;
import com.homegravity.Odi.global.response.success.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/notification")
public class NotificationController {

    private final NotificationService notificationService;

    @Operation(summary = "알림 목록 조회", description = "로그인한 사용자의 알림 목록을 조회합니다.")
    @GetMapping("")
    public ApiResponse<List<NotificationDTO>> getAllNotifications(@AuthenticationPrincipal Member member) {
        return ApiResponse.of(SuccessCode.NOTIFICATION_GET_SUCCESS, notificationService.getAllNotificationsByMember(member));
    }

    @Operation(summary = "알림 읽음 처리", description = "알림의 읽음 상태를 참으로 수정합니다.")
    @PutMapping("/{notification-id}")
    public ApiResponse<NotificationDTO> updateNotification(@PathVariable(value = "notification-id") Long notificationId) {
        return ApiResponse.of(SuccessCode.NOTIFICATION_UPDATE_SUCCESS, notificationService.updateNotification(notificationId));
    }

    // 알림 삭제
    @Operation(summary = "알림 삭제", description = "알림을 삭제합니다.")
    @DeleteMapping("/{notification-id}")
    public ApiResponse<Void> deleteNotification(@PathVariable(value = "notification-id") Long notificationId) {
        notificationService.deleteNotification(notificationId);
        return ApiResponse.of(SuccessCode.NOTIFICATION_DELETE_SUCCESS);
    }
}
