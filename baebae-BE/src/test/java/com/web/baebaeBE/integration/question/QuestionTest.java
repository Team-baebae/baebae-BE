package com.web.baebaeBE.integration.question;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.junit.jupiter.api.AfterEach;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@SpringBootTest()
@AutoConfigureMockMvc
@WithMockUser
@Transactional
public class QuestionTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @MockBean
    private QuestionService questionService;

    @Autowired
    private JwtTokenProvider tokenProvider;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private Member testMember;
    private Member testReceiver;
    private String refreshToken;
    private QuestionDetailResponse testQuestionDetailResponse;

    @BeforeEach
    void setup() {
        testMember = memberRepository.save(Member.builder()
                .email("test@gmail.com")
                .nickname("장지효")
                .memberType(MemberType.KAKAO)
                .refreshToken("null")
                .build());

        testReceiver = memberRepository.save(Member.builder()
                .email("test@gmail2.com")
                .nickname("장지효2")
                .memberType(MemberType.KAKAO)
                .refreshToken("null")
                .build());


        refreshToken = tokenProvider.generateToken(testMember, Duration.ofDays(14));

        testMember.updateRefreshToken(refreshToken);
        memberRepository.save(testMember);

        refreshToken = tokenProvider.generateToken(testReceiver, Duration.ofDays(14));

        testReceiver.updateRefreshToken(refreshToken);
        memberRepository.save(testReceiver);

    }

    @AfterEach
    void tearDown() {
        Optional<Member> member = memberRepository.findByEmail("test@gmail.com");
        member.ifPresent(memberRepository::delete);
    }

    @Test
    public void createQuestionTest() throws Exception {

        // Given
        String content = "이것은 회원의 질문입니다.";
        QuestionDetailResponse questionDetailResponse = new QuestionDetailResponse(1L, content, "장지효", true, LocalDateTime.now(), false);
        Long senderId = testMember.getId();
        Long receiverId = 2L;
        QuestionCreateRequest createRequest = new QuestionCreateRequest(content, "장지효", true);
        String jsonRequest = objectMapper.writeValueAsString(createRequest);

        // When
        when(questionService.createQuestion(any(QuestionCreateRequest.class), eq(senderId), eq(receiverId)))
                .thenReturn(questionDetailResponse);

        // Then
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
        QuestionDetailResponse questionDetailResponse = new QuestionDetailResponse(1L, content, "닉네임", true, LocalDateTime.now(), true);
        List<QuestionDetailResponse> questionDetailResponseList = List.of(questionDetailResponse);
        Page<QuestionDetailResponse> questionDetailResponsePage = new PageImpl<>(questionDetailResponseList, Pageable.unpaged(), 1);

        // Mock 설정
        when(questionService.getQuestionsByMemberId(eq(testReceiver.getId()), any(Pageable.class)))
                .thenReturn(questionDetailResponsePage);

        mockMvc.perform(get("/api/questions/member/{memberId}", testReceiver.getId())
                        .header("Authorization", "Bearer " + refreshToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].content").value(content))
                .andExpect(jsonPath("$[0].nickname").value("닉네임"))
                .andExpect(jsonPath("$[0].profileOnOff").value(true));
    }


    @Test
    @DisplayName("질문 수정 테스트(): 질문을 수정한다.")
    public void updateQuestionTest() throws Exception {
        // 수정 전 질문 생성
        String content = "이것은 수정 전의 질문입니다.";
        Question question = questionRepository.save(new Question(null, testMember,testReceiver,content, "닉네임", true, LocalDateTime.now(), true));
        String updatedContent = "이것은 수정 후의 질문입니다.";

        // 질문 수정 요청을 보내고 응답을 확인
        mockMvc.perform(MockMvcRequestBuilders.put("/api/questions/{questionId}", question.getId())
                        .param("content", updatedContent)
                        .param("isAnswered", "true")
                        .header("Authorization", "Bearer " + refreshToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("질문 삭제 테스트(): 질문을 삭제한다.")
    public void deleteQuestionTest() throws Exception {

        String content = "이것은 삭제할 질문입니다.";
        Question question = questionRepository.save(new Question(null, testMember, testReceiver, content, "닉네임", true, LocalDateTime.now(), true));

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/questions/{questionId}", question.getId())
                        .header("Authorization", "Bearer " + refreshToken)
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isNoContent());
    }
}
