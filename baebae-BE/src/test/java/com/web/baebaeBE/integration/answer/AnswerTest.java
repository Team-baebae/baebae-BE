package com.web.baebaeBE.integration.answer;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.web.baebaeBE.global.jwt.JwtTokenProvider;
import com.web.baebaeBE.infra.answer.entity.Answer;
import com.web.baebaeBE.infra.answer.repository.AnswerRepository;
import com.web.baebaeBE.infra.member.entity.Member;
import com.web.baebaeBE.infra.member.enums.MemberType;
import com.web.baebaeBE.infra.member.repository.MemberRepository;
import com.web.baebaeBE.infra.question.entity.Question;
import com.web.baebaeBE.infra.question.repository.QuestionRepository;
import com.web.baebaeBE.presentation.answer.dto.AnswerCreateRequest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest()
@AutoConfigureMockMvc
@WithMockUser
@Transactional

public class AnswerTest {
    @Mock
    private AnswerRepository answerRepository;
    @Mock
    private QuestionRepository questionRepository;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private JwtTokenProvider tokenProvider;
    private String refreshToken;
    private Member testMember;

    @BeforeEach
    void setup() {
        testMember = memberRepository.save(Member.builder()
                .email("test@gmail.com")
                .nickname("장지효")
                .memberType(MemberType.KAKAO)
                .refreshToken("null")
                .build());
        refreshToken = tokenProvider.generateToken(testMember, Duration.ofDays(14)); // 임시 refreshToken 생성

        testMember.updateRefreshToken(refreshToken);
        memberRepository.save(testMember);
    }

    @AfterEach
    void tearDown() {
        Optional<Member> member = memberRepository.findByEmail("test@gmail.com");
        member.ifPresent(memberRepository::delete);
    }


    @Test
    @DisplayName("답변 생성 테스트: 답변을 생성합니다")
    public void createAnswerTest() throws Exception {
        // Given
        AnswerCreateRequest request = new AnswerCreateRequest();
        request.setQuestionId(1L);
        request.setContent("테스트 답변 내용");
        request.setLinkAttachments(Collections.singletonList("https://example.com"));
        request.setMusicName("테스트 음악");

        MockMultipartFile file = new MockMultipartFile("imageFiles", "test.jpg", MediaType.IMAGE_JPEG_VALUE, "이미지 데이터".getBytes());

        // When & Then
        mockMvc.perform(multipart("/api/answers/member/{memberId}", testMember.getId())
                        .file(file)
                        .header("Authorization", "Bearer "+ refreshToken)
                        .param("questionId", String.valueOf(request.getQuestionId()))
                        .param("content", request.getContent())
                        .param("linkAttachments", objectMapper.writeValueAsString(request.getLinkAttachments()))
                        .param("musicName", request.getMusicName()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());
    }


    @Test
    @DisplayName("답변 조회 테스트: 답변을 조회합니다")
    public void getAnswerTest() throws Exception {
        // Given
        Question question = Question.builder()
                .id(1L)
                .build();
        // 모의 객체를 사용하여 questionRepository 대체
        when(questionRepository.findById(anyLong())).thenReturn(Optional.of(question));

        Answer answer = Answer.builder()
                .question(question)
                .member(testMember)
                .content("테스트 답변 내용")
                .imageFiles(Collections.singletonList("https://example.com/image.jpg"))
                .musicName("테스트 음악")
                .createdDate(LocalDateTime.now())
                .build();

        // 이미지 파일
        byte[] imageBytes = "이미지 데이터".getBytes();
        MockMultipartFile imageFile = new MockMultipartFile("imageFile", "image.jpg", MediaType.IMAGE_JPEG_VALUE, imageBytes);

        // When & Then
        mockMvc.perform(
                        multipart("/api/answers/{answerId}", answer.getId())
                                .file(imageFile)
                                .header("Authorization", "Bearer "+ refreshToken)
                                .contentType(MediaType.MULTIPART_FORM_DATA)
                                .param("content", answer.getContent())
                                .param("musicName", answer.getMusicName()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(answer.getId()))
                .andExpect(jsonPath("$.content").value(answer.getContent()));

    }

    @Test
    @DisplayName("답변 수정 테스트: 답변을 수정합니다")
    public void updateAnswerTest() throws Exception {
        Question question = Question.builder()
                .id(1L)
                .build();
        // 모의 객체를 사용하여 questionRepository 대체
        when(questionRepository.findById(anyLong())).thenReturn(Optional.of(question));
        // Given
        Answer answer = answerRepository.save(Answer.builder()
                .question(question)
                .member(testMember)
                .content("수정 전 답변 내용")
                .imageFiles(Collections.singletonList("https://example.com/image.jpg"))
                .musicName("테스트 음악")
                .createdDate(LocalDateTime.now())
                .build());

        AnswerCreateRequest request = new AnswerCreateRequest();
        request.setContent("수정된 답변 내용");
        request.setLinkAttachments(Collections.singletonList("https://updated-example.com"));
        request.setMusicName("업데이트된 테스트 음악");

        byte[] imageBytes = "이미지 데이터".getBytes();
        MockMultipartFile imageFile = new MockMultipartFile("imageFile", "image.jpg", MediaType.IMAGE_JPEG_VALUE, imageBytes);

        // When & Then
        mockMvc.perform(multipart("/api/answers/{answerId}", answer.getId())
                        .file(new MockMultipartFile("imageFile", "image.jpg", MediaType.IMAGE_JPEG_VALUE, imageBytes))
                        .header("Authorization", "Bearer "+ refreshToken)
                        .param("content", request.getContent())
                        .param("linkAttachments", objectMapper.writeValueAsString(request.getLinkAttachments()))
                        .param("musicName", request.getMusicName()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(answer.getId()))
                .andExpect(jsonPath("$.content").value(request.getContent()));
    }



    @Test
    @DisplayName("답변 삭제 테스트(): 답변을 삭제한다.")
    public void deleteAnswerTest() throws Exception {
        // given
        Answer savedAnswer = answerRepository.save(Answer.builder()
                .question(null) // 테스트 시에는 question을 null로 설정
                .member(testMember)
                .content("답변 내용")
                .imageFiles(Collections.emptyList())
                .musicName("음악 제목")
                .musicSinger("가수 이름")
                .musicAudio("음악 오디오 URL")
                .linkAttachments(Collections.emptyList())
                .heartCount(0)
                .curiousCount(0)
                .sadCount(0)
                .build());

        // when
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/answer/{answerId}", savedAnswer.getId())
                        .header("Authorization", "Bearer "+ refreshToken)
                        .contentType(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isOk());
    }
}
