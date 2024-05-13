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

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {

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
                .questionContent(createNotificationDto.getQuestionContent())
                .build());

        String token = member.getFcmToken();
        if (token != null && !token.isEmpty()) {
            switch (createNotificationDto.getEventType()) {
                case NEW_QUESTION:
                    sendNotificationToUser(member.getId(), "새 질문이 도착했습니다!", member.getNickname() + "님, 새로운 질문을 확인하세요: " + createNotificationDto.getQuestionContent());
                    break;
                case NEW_ANSWER:
                    sendNotificationToUser(member.getId(), "새로운 답변이 도착했습니다!", member.getNickname() + "의 질문 \"" + createNotificationDto.getQuestionContent() + "\"에 새로운 답변이 등록되었습니다.");
                    break;
                case REACTION:
                    sendNotificationToUser(member.getId(), "반응 알림!", member.getNickname() + "님의 답변에 " + createNotificationDto.getReactionType() + " 반응이 있습니다: " + createNotificationDto.getNotificationContent());
                    break;
            }
        }

        return notification;
    }

    public void sendNotificationToUser(Long memberId, String title, String body) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));
        String token = member.getFcmToken();
        if (token != null && !token.isEmpty()) {
            try {
                firebaseMessagingService.sendNotification(token, title, body);
            } catch (FirebaseMessagingException e) {
                log.error("Failed to send FCM notification", e);
            }
        }
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
