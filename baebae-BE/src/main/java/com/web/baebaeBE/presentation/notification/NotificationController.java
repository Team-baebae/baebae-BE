package com.web.baebaeBE.presentation.notification;

import com.web.baebaeBE.application.notification.NotificationApplication;
import com.web.baebaeBE.presentation.notification.api.NotificationApi;
import com.web.baebaeBE.presentation.notification.dto.NotificationResponse;
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

    private final NotificationApplication notificationApplication;


    // 특정 멤버의 모든 알람 조회
    @GetMapping("/member/{memberId}")
    public ResponseEntity<NotificationResponse.NotificationListResponse> getNotificationsListByMember(@PathVariable Long memberId) {
        NotificationResponse.NotificationListResponse notifications = notificationApplication.getNotificationsListByMember(memberId);

        return ResponseEntity.ok(notifications);
    }

    // 알람 세부정보 조회
    @GetMapping("/{notificationId}")
    public ResponseEntity<NotificationResponse.NotificationContentResponse> getNotificationById(@PathVariable Long notificationId) {
        NotificationResponse.NotificationContentResponse notification = notificationApplication.getNotificationById(notificationId);

        return ResponseEntity.ok(notification);
    }
}
