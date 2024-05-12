package com.web.baebaeBE.integration.notification;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.baebaeBE.domain.notification.service.NotificationService;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest()
@AutoConfigureMockMvc
@WithMockUser
@Transactional
public class NotificationManageTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private JwtTokenProvider tokenProvider;

    private ObjectMapper objectMapper = new ObjectMapper();
    private String accessToken;
    private String refreshToken;
    private Member testMember;

    //각 테스트 전마다 가짜 유저 생성 및 토큰 발급
    @BeforeEach
    void setup() {
        testMember = memberRepository.save(Member.builder()
                .email("test@gmail.com")
                .nickname("김예찬")
                .memberType(MemberType.KAKAO)
                .refreshToken("null")
                .build());

        accessToken = tokenProvider.generateToken(testMember, Duration.ofDays(1)); // 임시 accessToken 생성
        refreshToken = tokenProvider.generateToken(testMember, Duration.ofDays(14)); // 임시 refreshToken 생성

        testMember.updateRefreshToken(refreshToken);
        memberRepository.save(testMember);
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
    void createNotificationTest() {
        // Given
        NotificationRequest.create createRequest = new NotificationRequest.create(
                testMember.getId(),
                "배승우님이 질문을 남기셨습니다! 확인해보세요",
                "가은아! 넌 무슨색상을 좋아해?"
        );

        // when
        Notification createdNotification = notificationService.createNotification(createRequest);

        // then
        assertNotNull(createdNotification);
        assertNotNull(createdNotification.getId()); // 알람 ID가 null이 아니어야 함
        assertEquals("배승우님이 질문을 남기셨습니다! 확인해보세요", createdNotification.getNotificationContent());
        assertEquals("가은아! 넌 무슨색상을 좋아해?", createdNotification.getQuestionContent());
        assertEquals(testMember, createdNotification.getMember()); // 알람이 해당 멤버에 연결되어 있는지 확인
    }


    @Test
    @DisplayName("알림 삭제 테스트(): 새로운 알림을 생성하고 삭제한다.")
    void deleteNotificationTest() {
        // Given
        NotificationRequest.create createRequest = new NotificationRequest.create(
                testMember.getId(),
                "배승우님이 질문을 남기셨습니다! 확인해보세요",
                "가은아! 넌 무슨색상을 좋아해?"
        );
        Notification createdNotification = notificationService.createNotification(createRequest);

        // When
        notificationService.deleteNotification(createdNotification.getId());

        // Then
        assertTrue(notificationRepository.findById(createdNotification.getId()).isEmpty());
    }

}
