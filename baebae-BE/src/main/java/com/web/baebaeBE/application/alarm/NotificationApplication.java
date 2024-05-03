package com.web.baebaeBE.application.notification;

import com.web.baebaeBE.domain.notification.service.NotificationService;
import com.web.baebaeBE.presentation.notification.dto.NotificationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class NotificationApplication {

    private final NotificationService notificationService;


    // 특정 멤버의 모든 알람 조회
    public NotificationResponse.NotificationListResponse getNotificationsListByMember(Long memberId) {
       return notificationService.getNotificationsListByMember(memberId);
    }

    // 알림 세부정보 조회
    public NotificationResponse.NotificationContentResponse getNotificationById(Long notificationId) {
        return notificationService.getNotificationById(notificationId);
    }

}
