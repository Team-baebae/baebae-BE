package com.web.baebaeBE.global.firebase;

import com.google.firebase.ErrorCode;
import com.google.firebase.messaging.MessagingErrorCode;
import com.web.baebaeBE.domain.fcm.entity.FcmToken;
import com.web.baebaeBE.domain.fcm.repository.FcmTokenRepository;
import com.web.baebaeBE.domain.fcm.service.FcmService;
import com.web.baebaeBE.domain.member.entity.Member;
import com.web.baebaeBE.domain.answer.entity.Answer;
import com.web.baebaeBE.domain.notification.dto.NotificationRequest;
import com.web.baebaeBE.domain.notification.service.NotificationService;
import com.web.baebaeBE.domain.question.entity.Question;
import com.web.baebaeBE.domain.reaction.entity.MemberAnswerReaction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class FirebaseNotificationService {
    private final FirebaseMessagingService firebaseMessagingService;
    private final FcmTokenRepository fcmTokenRepository;
    private final FcmService fcmService;
    private final NotificationService notificationService;


    public void notifyNewQuestion(Member member, Question question) {
        String notificationTitle = question.getNickname() + "님이 질문을 남겼어요!\n";
        String notificationBody = question.getContent();

        // 알림 생성 및 전송
        NotificationRequest.create notificationDto = new NotificationRequest.create(
                member.getId(),
                question.getNickname() + "님이 질문을 남겼어요!",
                question.getContent(),
                NotificationRequest.EventType.NEW_ANSWER,
                null
        );
        notificationService.createNotification(notificationDto);

        // 모든 fcm 토큰 가져오기
        List<FcmToken> fcmTokens = fcmTokenRepository.findByMemberId(member.getId());

        for (FcmToken fcmToken : fcmTokens) {
            fcmService.updateLastUsedTime(fcmToken);
            sendNotificationToUser(fcmToken, notificationTitle, notificationBody);
        }
    }
    public void notifyNewAnswer(Member member, Question question, Answer answer) {
        String notificationTitle = member.getNickname() + "님이 질문에 답변을 남겼어요!\n";
        String notificationBody = answer.getContent();

        // 알림 생성 및 전송
        NotificationRequest.create notificationDto = new NotificationRequest.create(
                member.getId(),
                member.getNickname() + "님이 질문에 답변을 남겼어요!",
                question.getContent(),
                NotificationRequest.EventType.NEW_ANSWER,
                null
        );
        notificationService.createNotification(notificationDto);

        // 모든 fcm 토큰 가져오기
        List<FcmToken> fcmTokens = fcmTokenRepository.findByMemberId(question.getSender().getId());

        System.out.println(question.getSender().getId() + "   " + question.getSender().getNickname());
        for (FcmToken fcmToken : fcmTokens) {
            fcmService.updateLastUsedTime(fcmToken);
            System.out.println("     "+fcmToken.getToken());
            sendNotificationToUser(fcmToken, notificationTitle, notificationBody);
        }
    }


    public void notifyReaction(Member member, Answer answer, MemberAnswerReaction reaction) {
        String emoticon = "";
        switch (reaction.getReaction()) {
            case HEART:
                emoticon = "❤";
                break;
            case CURIOUS:
                emoticon = "👀";
                break;
            case SAD:
                emoticon = "😢";
                break;
            case CONNECT:
                emoticon = "👉👈";
                break;
        }
        String notificationTitle = member.getNickname()+"님이 " + emoticon + "을 남겼어요!\n";

        //알림 생성 및 저장
        NotificationRequest.create notificationDto = new NotificationRequest.create(
                answer.getMember().getId(),
                notificationTitle,
                answer.getContent(),
                NotificationRequest.EventType.NEW_ANSWER,
                reaction.getReaction()
        );
        notificationService.createNotification(notificationDto);

        // 모든 fcm 토큰 가져오기
        List<FcmToken> fcmTokens = fcmTokenRepository.findByMemberId(answer.getMember().getId());

        for (FcmToken fcmToken : fcmTokens) {
            fcmService.updateLastUsedTime(fcmToken);
            sendNotificationToUser(fcmToken, notificationTitle, "");
        }
    }

    private void sendNotificationToUser(FcmToken fcmToken, String title, String body) {
        String response = firebaseMessagingService.sendNotification(fcmToken.getToken(), title, body);
        if (MessagingErrorCode.INVALID_ARGUMENT.name().equals(response) || MessagingErrorCode.UNREGISTERED.name().equals(response)) {
            // 토큰이 유효하지 않은 경우, 삭제
            fcmTokenRepository.delete(fcmToken);
            log.info("유효하지않은 토큰 {} 삭제", fcmToken.getToken());
        } else {
            log.info(fcmToken.getToken());
        }
    }
}