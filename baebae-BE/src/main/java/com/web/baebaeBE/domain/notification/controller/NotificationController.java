package com.web.baebaeBE.domain.notification.controller;

import com.web.baebaeBE.domain.notification.controller.api.NotificationApi;
import com.web.baebaeBE.domain.notification.dto.NotificationResponse;
import com.web.baebaeBE.domain.notification.service.NotificationService;
import com.web.baebaeBE.global.authorization.annotation.AuthorizationMember;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications") // 모든 경로가 "/api/notifications"로 시작합니다.
public class NotificationController implements NotificationApi {

    private final NotificationService notificationService;


    // 특정 멤버의 모든 알람 조회
    @GetMapping("/member/{memberId}")
    @AuthorizationMember
    public ResponseEntity<NotificationResponse.NotificationListResponse> getNotificationsListByMember(@PathVariable Long memberId) {

        return ResponseEntity.ok(notificationService.getNotificationsListByMember(memberId));
    }

}
