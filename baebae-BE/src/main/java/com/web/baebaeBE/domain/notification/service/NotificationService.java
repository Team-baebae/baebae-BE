package com.web.baebaeBE.domain.notification.service;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.web.baebaeBE.domain.login.exception.LoginException;
import com.web.baebaeBE.global.error.exception.BusinessException;
import com.web.baebaeBE.domain.notification.entity.Notification;
import com.web.baebaeBE.domain.notification.repository.NotificationRepository;
import com.web.baebaeBE.domain.member.entity.Member;
import com.web.baebaeBE.domain.member.repository.MemberRepository;
import com.web.baebaeBE.domain.notification.dto.NotificationRequest;
import com.web.baebaeBE.domain.notification.dto.NotificationResponse;
import com.web.baebaeBE.global.firebase.FirebaseMessagingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {//

    private final NotificationRepository notificationRepository;
    private final MemberRepository memberRepository;
    private final FirebaseMessagingService firebaseMessagingService;

    // 알림 생성 및 FCM 알림 전송
    public Notification createNotification(NotificationRequest.create createNotificationDto) {
        Member member = memberRepository.findById(createNotificationDto.getMemberId())
                .orElseThrow(() -> new BusinessException(LoginException.NOT_EXIST_MEMBER));

        Notification notification = notificationRepository.save(Notification.builder()
                .member(member)
                .notificationContent(createNotificationDto.getNotificationContent())
                .detailContent(createNotificationDto.getDetailContent())
                .createdTime(LocalDateTime.now())
                .isChecked(false)
                .build());

        return notification;
    }


    // 특정 멤버의 모든 알람 조회
    public NotificationResponse.NotificationListResponse getNotificationsListByMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(LoginException.NOT_EXIST_MEMBER));

        List<Notification> notificationList = notificationRepository.findByMemberOrderByNotificationTimeDesc(member);

        return  NotificationResponse.NotificationContentResponse.ListOf(notificationList);
    }

    // 알림 세부정보 조회
    public NotificationResponse.NotificationContentResponse getNotificationById(Long notificationId) {
        return NotificationResponse.NotificationContentResponse
                .of(notificationRepository.findById(notificationId).get());
    }


    // 알람 삭제
    public void deleteNotification(Long notificationId) {
        notificationRepository.deleteById(notificationId);
    }
}
