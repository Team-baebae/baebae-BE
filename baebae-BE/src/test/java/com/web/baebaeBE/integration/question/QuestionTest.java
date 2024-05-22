package com.web.baebaeBE.integration.question;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.baebaeBE.domain.oauth2.controller.Oauth2Controller;
import com.web.baebaeBE.domain.question.dto.QuestionDetailResponse;
import com.web.baebaeBE.domain.question.entity.Question;
import com.web.baebaeBE.domain.question.service.QuestionService;
import com.web.baebaeBE.global.jwt.JwtTokenProvider;
import com.web.baebaeBE.domain.member.entity.Member;
import com.web.baebaeBE.domain.member.entity.MemberType;
import com.web.baebaeBE.domain.member.repository.MemberRepository;
import com.web.baebaeBE.domain.question.repository.QuestionRepository;
import com.web.baebaeBE.domain.question.dto.QuestionCreateRequest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@SpringBootTest()
@AutoConfigureMockMvc
@WithMockUser
@ActiveProfiles("test")
@Transactional
public class QuestionTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private MemberRepository memberRepository;
    @MockBean
    private QuestionRepository questionRepository;
    @MockBean
    private QuestionService questionService;

    @Autowired
    private JwtTokenProvider tokenProvider;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private Oauth2Controller oauth2Controller;
    private Member testMember;
    private Member testReceiver;
    private String refreshToken;
    private String refreshTokenReceiver;
    private QuestionDetailResponse testQuestionDetailResponse;

    @BeforeEach
    void setup() {
        testMember = Member.builder()
                .id(1L)
                .email("test@gmail.com")
                .nickname("장지효")
                .memberType(MemberType.KAKAO)
                .refreshToken("null")
                .build();

        testReceiver = Member.builder()
                .id(2L)
                .email("test@gmail2.com")
                .nickname("장지효2")
                .memberType(MemberType.KAKAO)
                .refreshToken("null")
                .build();

        when(memberRepository.save(any(Member.class))).thenReturn(testMember);
        when(memberRepository.findByEmail("test@gmail.com")).thenReturn(Optional.of(testMember));
        when(memberRepository.findById(1L)).thenReturn(Optional.of(testMember));

        when(memberRepository.save(any(Member.class))).thenReturn(testReceiver);
        when(memberRepository.findByEmail("test@gmail2.com")).thenReturn(Optional.of(testReceiver));
        when(memberRepository.findById(1L)).thenReturn(Optional.of(testReceiver));

        refreshToken = tokenProvider.generateToken(testMember, Duration.ofDays(14));  // 임시 refreshToken 생성
        refreshToken = tokenProvider.generateToken(testReceiver, Duration.ofDays(14));

        testMember.updateRefreshToken(refreshToken);
        when(memberRepository.save(testMember)).thenReturn(testMember);

        testReceiver.updateRefreshToken(refreshToken);
        when(memberRepository.save(testReceiver)).thenReturn(testReceiver);

        when(memberRepository.findByRefreshToken(refreshToken)).thenReturn(Optional.of(testMember));
        when(memberRepository.findByRefreshToken(refreshToken)).thenReturn(Optional.of(testReceiver));
    }


    @Test
    @DisplayName("질문 생성 테스트(): 질문을 생성한다.")
    public void createQuestionTest() throws Exception {
        String content = "이것은 회원의 질문입니다.";
        QuestionDetailResponse questionDetailResponse = new QuestionDetailResponse(1L, content, "장지효", "장지효", true, LocalDateTime.now(), false);
        Long senderId = 1L;
        Long receiverId = 2L;
        QuestionCreateRequest createRequest = new QuestionCreateRequest(content, "장지효", true);
        String jsonRequest = objectMapper.writeValueAsString(createRequest);

        when(questionService.createQuestion(any(QuestionCreateRequest.class), eq(senderId), eq(receiverId)))
                .thenReturn(questionDetailResponse);

        mockMvc.perform(post("/api/questions/sender/{senderId}/receiver/{receiverId}", senderId, receiverId)
                        .header("Authorization", "Bearer " + refreshToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.content").value("이것은 회원의 질문입니다."))
                .andExpect(jsonPath("$.nickname").value("장지효"))
                .andExpect(jsonPath("$.profileOnOff").value(true));
    }

    @Test
    @DisplayName("회원별 질문 조회 테스트(): 해당 회원의 질문을 조회한다.")
    public void getQuestionsByMemberIdTest() throws Exception {
        // Mock 응답 설정
        String content = "이것은 회원의 질문입니다.";
        QuestionDetailResponse questionDetailResponse = new QuestionDetailResponse(1L, content, "닉네임", "장지효",true, LocalDateTime.now(), true);
        Long memberId = 1L;
        List<QuestionDetailResponse> questionDetailResponseList = List.of(questionDetailResponse);
        Page<QuestionDetailResponse> questionDetailResponsePage = new PageImpl<>(questionDetailResponseList, Pageable.unpaged(), 1);

        // Mock 설정
        when(questionService.getQuestionsByMemberId(eq(memberId), any(Pageable.class)))
                .thenReturn(questionDetailResponsePage);

        mockMvc.perform(get("/api/questions/member/{memberId}", memberId)
                        .header("Authorization", "Bearer " + refreshToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].content").value(content))
                .andExpect(jsonPath("$[0].nickname").value("닉네임"))
                .andExpect(jsonPath("$[0].profileOnOff").value(true));
    }



    @Test
    @DisplayName("질문 삭제 테스트(): 질문을 삭제한다.")
    public void deleteQuestionTest() throws Exception {

        // 질문 생성 및 저장
        Question question = Question.builder()
                .id(1L)
                .sender(testMember)
                .receiver(testReceiver)
                .content("이것은 삭제할 질문입니다.")
                .nickname("닉네임")
                .profileOnOff(true)
                .createdDate(LocalDateTime.now())
                .build();

        when(questionRepository.save(any(Question.class))).thenReturn(question);
        question = questionRepository.save(question);

        when(questionRepository.findById(1L)).thenReturn(Optional.of(question));
        doNothing().when(questionService).deleteQuestion(eq(1L));

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/questions/{questionId}", question.getId())
                        .header("Authorization", "Bearer " + refreshToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}

