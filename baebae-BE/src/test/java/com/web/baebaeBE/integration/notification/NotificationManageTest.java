package com.web.baebaeBE.integration.notification;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.web.baebaeBE.domain.notification.service.NotificationService;
import com.web.baebaeBE.domain.oauth2.controller.Oauth2Controller;
import com.web.baebaeBE.global.firebase.FirebaseMessagingService;
import com.web.baebaeBE.global.jwt.JwtTokenProvider;
import com.web.baebaeBE.domain.notification.entity.Notification;
import com.web.baebaeBE.domain.notification.repository.NotificationRepository;
import com.web.baebaeBE.domain.member.entity.Member;
import com.web.baebaeBE.domain.member.entity.MemberType;
import com.web.baebaeBE.domain.member.repository.MemberRepository;
import com.web.baebaeBE.domain.notification.dto.NotificationRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest()
@AutoConfigureMockMvc
@WithMockUser
@Transactional
public class NotificationManageTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private NotificationRepository notificationRepository;
    @MockBean
    private NotificationService notificationService;
    @MockBean
    private MemberRepository memberRepository;
    @Autowired
    private JwtTokenProvider tokenProvider;
    @MockBean
    private Oauth2Controller oauth2Controller;


    private ObjectMapper objectMapper = new ObjectMapper();
    private String accessToken;
    private String refreshToken;
    private Member testMember;

    @MockBean // FirebaseMessagingService를 Mock으로 선언
    private FirebaseMessagingService mockFirebaseMessagingService;

    //각 테스트 전마다 가짜 유저 생성 및 토큰 발급
    @BeforeEach
    void setup() {
        testMember = Member.builder()  // 클래스 필드 testMember를 초기화
                .id(1L)  // ID를 명시적으로 설정
                .email("test@gmail.com")
                .nickname("test")
                .memberType(MemberType.KAKAO)
                .refreshToken("null")
                .build();

        when(memberRepository.save(any(Member.class))).thenReturn(testMember);
        when(memberRepository.findByEmail("test@gmail.com")).thenReturn(Optional.of(testMember));
        when(memberRepository.findById(1L)).thenReturn(Optional.of(testMember));

        accessToken = tokenProvider.generateToken(testMember, Duration.ofDays(1));  // 임시 accessToken 생성
        refreshToken = tokenProvider.generateToken(testMember, Duration.ofDays(14));  // 임시 refreshToken 생성

        testMember.updateRefreshToken(refreshToken);
        when(memberRepository.save(testMember)).thenReturn(testMember);

        when(memberRepository.findByRefreshToken(refreshToken)).thenReturn(Optional.of(testMember));
    }

    //각 테스트 후마다 가짜유저 데이터 삭제
    @AfterEach
    void tearDown() {
        Optional<Member> member = memberRepository.findByEmail("test@gmail.com");
        if(member.isPresent())
            memberRepository.delete(member.get());
    }

    @Test
    @DisplayName("알림 생성 테스트(): 테스트 회원으로 새로운 알림을 생성한다.")
    void createNotificationTest() throws FirebaseMessagingException {
        // Given
        NotificationRequest.create createRequest = new NotificationRequest.create(
                testMember.getId(),
                "배승우님이 질문을 남기셨습니다! 확인해보세요",
                "가은아! 넌 무슨색상을 좋아해?",
                NotificationRequest.EventType.NEW_QUESTION, // 이벤트 타입 설정
                null
        );

        Notification mockNotification = Notification.builder()
                .id(1L)
                .member(testMember)
                .notificationContent("배승우님이 질문을 남기셨습니다! 확인해보세요")
                .detailContent("가은아! 넌 무슨색상을 좋아해?")
                .build();

        when(notificationService.createNotification(any(NotificationRequest.create.class)))
                .thenAnswer(invocation -> {
                    NotificationRequest.create request = invocation.getArgument(0);
                    // 파이어베이스 호출
                    mockFirebaseMessagingService.sendNotification(
                            testMember.getRefreshToken(),
                            request.getNotificationContent(),
                            request.getDetailContent()
                    );
                    return mockNotification;
                });

        when(mockFirebaseMessagingService.sendNotification(anyString(), anyString(), anyString()))
                .thenReturn("mock_message_id");

        // when
        Notification createdNotification = notificationService.createNotification(createRequest);

        // then
        assertNotNull(createdNotification);
        assertNotNull(createdNotification.getId()); // 알람 ID가 null이 아니어야 함
        assertEquals("배승우님이 질문을 남기셨습니다! 확인해보세요", createdNotification.getNotificationContent());
        assertEquals("가은아! 넌 무슨색상을 좋아해?", createdNotification.getDetailContent());
        assertEquals(testMember, createdNotification.getMember()); // 알람이 해당 멤버에 연결되어 있는지 확인

        verify(mockFirebaseMessagingService).sendNotification(
                eq(testMember.getRefreshToken()), // FCM 토큰이나 필요한 식별자
                eq("배승우님이 질문을 남기셨습니다! 확인해보세요"),
                eq("가은아! 넌 무슨색상을 좋아해?")
        );
    }
}