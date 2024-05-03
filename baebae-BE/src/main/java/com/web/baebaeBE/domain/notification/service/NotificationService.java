package com.web.baebaeBE.domain.notification.service;

import com.web.baebaeBE.domain.member.exception.MemberError;
import com.web.baebaeBE.global.error.exception.BusinessException;
import com.web.baebaeBE.infra.notification.entity.Notification;
import com.web.baebaeBE.infra.notification.repository.NotificationRepository;
import com.web.baebaeBE.infra.member.entity.Member;
import com.web.baebaeBE.infra.member.repository.MemberRepository;
import com.web.baebaeBE.presentation.notification.dto.NotificationRequest;
import com.web.baebaeBE.presentation.notification.dto.NotificationResponse;
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

    // 알림 생성
    public Notification createNotification(NotificationRequest.create createNotificationDto) {
        Member member = memberRepository.findById(createNotificationDto.getMemberId())
                .orElseThrow(() -> new BusinessException(MemberError.NOT_EXIST_MEMBER));

        return notificationRepository.save(Notification.builder()
                .member(member)
                .notificationContent(createNotificationDto.getNotificationContent())
                .questionContent(createNotificationDto.getQuestionContent())
                .build());
    }


    // 특정 멤버의 모든 알람 조회
    public NotificationResponse.NotificationListResponse getNotificationsListByMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(MemberError.NOT_EXIST_MEMBER));

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
