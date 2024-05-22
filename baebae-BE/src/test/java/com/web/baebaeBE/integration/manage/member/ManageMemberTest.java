package com.web.baebaeBE.integration.manage.member;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.baebaeBE.domain.fcm.dto.FcmRequest;
import com.web.baebaeBE.domain.fcm.entity.FcmToken;
import com.web.baebaeBE.domain.fcm.repository.FcmTokenRepository;
import com.web.baebaeBE.domain.fcm.service.FcmService;
import com.web.baebaeBE.domain.oauth2.controller.Oauth2Controller;
import com.web.baebaeBE.domain.question.dto.QuestionCreateRequest;
import com.web.baebaeBE.global.jwt.JwtTokenProvider;
import com.web.baebaeBE.domain.member.entity.Member;
import com.web.baebaeBE.domain.member.entity.MemberType;
import com.web.baebaeBE.domain.member.repository.MemberRepository;
import com.web.baebaeBE.domain.member.dto.MemberRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest()
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@WithMockUser
@Transactional
public class ManageMemberTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JwtTokenProvider tokenProvider;
    @MockBean
    private MemberRepository memberRepository;
    @MockBean
    private Oauth2Controller oauth2Controller;
    @MockBean
    private FcmService fcmService;
    @MockBean
    private FcmTokenRepository fcmTokenRepository;
    private ObjectMapper objectMapper = new ObjectMapper();
    private String accessToken;
    private String refreshToken;
    private Member testMember;


    @BeforeEach
    void setup() {
        // 새로운 멤버 객체를 생성하지만, 실제 저장된 객체의 ID를 반환하도록 Mock 설정
        Member initialMember = Member.builder()
                .email("test@gmail.com")
                .nickname("김예찬")
                .memberType(MemberType.KAKAO)
                .refreshToken("null")
                .build();

        Member savedMember = Member.builder()
                .id(1L) // Mocking된 저장된 객체의 ID 설정
                .email("test@gmail.com")
                .nickname("김예찬")
                .memberType(MemberType.KAKAO)
                .refreshToken("null")
                .build();

        when(memberRepository.save(any(Member.class))).thenReturn(savedMember);
        when(memberRepository.findByEmail("test@gmail.com")).thenReturn(Optional.of(savedMember));

        testMember = memberRepository.save(initialMember); // 실제 저장된 객체로 갱신

        accessToken = tokenProvider.generateToken(testMember, Duration.ofDays(1)); // 임시 accessToken 생성
        refreshToken = tokenProvider.generateToken(testMember, Duration.ofDays(14)); // 임시 refreshToken 생성

        testMember.updateRefreshToken(refreshToken);

        // 업데이트된 멤버 다시 저장
        when(memberRepository.save(testMember)).thenReturn(testMember);
        when(memberRepository.findById(testMember.getId())).thenReturn(Optional.of(testMember));
    }

    @AfterEach
    void tearDown() {
        Optional<Member> member = memberRepository.findByEmail("test@gmail.com");
        member.ifPresent(value -> memberRepository.delete(value));
    }

    @Test
    @DisplayName("회원정보 조회 테스트(): 해당 회원의 상세정보를 조회한다.")
    public void getMemberInformation() throws Exception {
        // given

        // when
        mockMvc.perform(get("/api/member/{memberId}", testMember.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.memberId").exists())
                .andExpect(jsonPath("$.email").exists())
                .andExpect(jsonPath("$.nickname").exists())
                .andExpect(jsonPath("$.memberType").exists());
    }
    @Test
    @DisplayName("프로필 사진 업데이트 테스트(): 회원의 프로필 사진을 업데이트한다.")
    public void updateProfileTest() throws Exception {
        // given
        File tempFile = File.createTempFile("temp_image", ".jpg"); // 임시 파일 생성

        try (OutputStream os = new FileOutputStream(tempFile)) {
            BufferedImage bufferedImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
            ImageIO.write(bufferedImage, "jpg", os);
        }
        byte[] imageBytes = Files.readAllBytes(tempFile.toPath()); // 사진 Byte 변환

        MockMultipartFile multipartFile = new MockMultipartFile(
                "image", // 파라미터 이름은 컨트롤러의 @RequestPart와 일치해야 함
                "image.jpg", // 파일 이름
                MediaType.IMAGE_JPEG_VALUE, // 컨텐트 타입
                imageBytes // 파일 바이트
        );

        // when
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/member/profile-image/{memberId}", testMember.getId())
                        .file(multipartFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .header("Authorization", "Bearer " + accessToken)
                        .with(request -> {
                            request.setMethod("PATCH"); // PATCH 메소드로 설정
                            return request;
                        }))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.imageUrl").exists());
    }

    @Test
    @DisplayName("FCM 토큰 추가 테스트(): 해당 회원에게 FCM 토큰을 추가한다.")
    public void addFcmTokenTest() throws Exception {
        // given
        FcmRequest.CreateToken tokenRequest = new FcmRequest.CreateToken("fwef094938jweSIJDe8204gaskd390GK32G9HADF0809d8708U908ud9UHD9FH4e32982hF0ODH22E");

        FcmToken fcmToken = FcmToken.builder()
                .id(1L)
                .token(tokenRequest.getFcmToken())
                .member(testMember)
                .lastUsedTime(LocalDateTime.now())
                .build();

        when(fcmTokenRepository.save(any(FcmToken.class))).thenReturn(fcmToken);
        when(fcmService.addFcmToken(testMember.getId(), tokenRequest.getFcmToken())).thenReturn(fcmToken);

        // when
        mockMvc.perform(post("/api/fcm/{memberId}", testMember.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tokenRequest))
                        .header("Authorization", "Bearer " + accessToken))
                // then
                .andExpect(status().isOk());
    }


    @Test
    @DisplayName("닉네임 업데이트 테스트(): 해당 회원의 닉네임을 업데이트한다.")
    public void updateNicknameTest() throws Exception {
        // given
        MemberRequest.UpdateNicknameDto updateNicknameDto
                = new MemberRequest.UpdateNicknameDto("새로운 닉네임");

        // when
        mockMvc.perform(patch("/api/member/nickname/{memberId}", testMember.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateNicknameDto))
                        .header("Authorization", "Bearer " + accessToken))
        // then
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("회원탈퇴 테스트(): 해당 회원의 정보를 영구적으로 삭제한다.")
    public void deleteMember() throws Exception {
        // given

        // when
        mockMvc.perform(delete("/api/member/{memberId}", testMember.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken))
        // then
                .andExpect(status().isOk());
    }

}
