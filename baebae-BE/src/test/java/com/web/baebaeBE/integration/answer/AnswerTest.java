package com.web.baebaeBE.integration.answer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.baebaeBE.domain.answer.dto.AnswerCreateRequest;
import com.web.baebaeBE.domain.answer.dto.AnswerDetailResponse;
import com.web.baebaeBE.domain.answer.dto.AnswerResponse;
import com.web.baebaeBE.domain.answer.service.AnswerService;
import com.web.baebaeBE.domain.member.entity.Member;
import com.web.baebaeBE.domain.member.entity.MemberType;
import com.web.baebaeBE.domain.member.repository.MemberRepository;
import com.web.baebaeBE.domain.question.entity.Question;
import com.web.baebaeBE.domain.question.repository.QuestionJpaRepository;
import com.web.baebaeBE.domain.question.repository.QuestionRepository;
import com.web.baebaeBE.global.jwt.JwtTokenProvider;
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
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
public class AnswerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuestionJpaRepository questionJpaRepository;

    @MockBean
    private AnswerService answerService;

    @Autowired
    private JwtTokenProvider tokenProvider;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private Member testMember;
    private Member testReceiver;
    private String refreshToken;
    private Question testQuestion;

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

        testQuestion = questionRepository.save(new Question(null, testMember, testReceiver,"이것은 질문입니다.", "장지효", true, LocalDateTime.now(), false));
    }

    @AfterEach
    void tearDown() {
        questionJpaRepository.deleteAll();
        Optional<Member> member = memberRepository.findByEmail("test@gmail.com");
        member.ifPresent(memberRepository::delete);
    }

    @Test
    @DisplayName("답변 생성 테스트(): 답변을 생성한다.")
    public void createAnswerTest() throws Exception {
        AnswerCreateRequest createRequest = new AnswerCreateRequest(
                testQuestion.getId(), "이것은 답변입니다.", "장지효", true, "https://link.com",
                "노래 제목", "가수 이름", "https://audio.url", "https://image.url", true
        );
        MockMultipartFile imageFile = new MockMultipartFile("imageFile", "image.jpg", MediaType.IMAGE_JPEG_VALUE, "image content".getBytes());
        MockMultipartFile requestFile = new MockMultipartFile("request", "", MediaType.APPLICATION_JSON_VALUE, objectMapper.writeValueAsBytes(createRequest));

        when(answerService.createAnswer(any(AnswerCreateRequest.class), eq(testMember.getId()), any(MockMultipartFile.class)))
                .thenReturn(new AnswerDetailResponse(
                        1L, testQuestion.getId(), testQuestion.getContent(), testMember.getId(), "이것은 답변입니다.",
                        testMember.getNickname(), "장지효", true, "https://link.com", "노래 제목", "가수 이름", "https://audio.url",
                        "https://image.url", LocalDateTime.now()
                ));

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/answers/{memberId}", testMember.getId())
                        .file(imageFile)
                        .file(requestFile)
                        .header("Authorization", "Bearer " + refreshToken)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("이것은 답변입니다."))
                .andExpect(jsonPath("$.nickname").value("장지효"))
                .andExpect(jsonPath("$.profileOnOff").value(true))
                .andExpect(jsonPath("$.linkAttachments").value("https://link.com"))
                .andExpect(jsonPath("$.musicName").value("노래 제목"))
                .andExpect(jsonPath("$.musicSinger").value("가수 이름"))
                .andExpect(jsonPath("$.musicAudioUrl").value("https://audio.url"))
                .andExpect(jsonPath("$.imageUrl").value("https://image.url"));

    }

    @Test
    @DisplayName("회원별 답변 조회 테스트(): 해당 회원의 답변을 조회한다.")
    public void getAnswersByMemberIdTest() throws Exception {
        AnswerResponse answerResponse = new AnswerResponse();
        List<AnswerResponse> answerResponseList = List.of(answerResponse);
        when(answerService.getAnswersByMemberId(testMember.getId())).thenReturn(answerResponseList);

        mockMvc.perform(get("/api/answers/member/{memberId}", testMember.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists());
    }

    @Test
    @DisplayName("모든 답변 조회 테스트(): 모든 답변을 조회한다.")
    public void getAllAnswersTest() throws Exception {
        AnswerDetailResponse answerDetailResponse = new AnswerDetailResponse(1L, testQuestion.getId(), testQuestion.getContent(), testMember.getId(), "이것은 답변입니다.", testMember.getNickname(), "장지효", true, "https://link.com", "노래 제목", "가수 이름", "https://audio.url", "https://image.url", LocalDateTime.now());
        List<AnswerDetailResponse> answerDetailResponseList = List.of(answerDetailResponse);
        Page<AnswerDetailResponse> answerDetailResponsePage = new PageImpl<>(answerDetailResponseList, Pageable.unpaged(), 1);

        when(answerService.getAllAnswers(eq(testMember.getId()), any(Long.class), any(Pageable.class))).thenReturn(answerDetailResponsePage);

        mockMvc.perform(get("/api/answers")
                        .param("memberId", String.valueOf(testMember.getId()))
                        .param("categoryId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].content").value("이것은 답변입니다."));
    }



    @Test
    @DisplayName("답변 수정 테스트(): 답변을 수정한다.")
    public void updateAnswerTest() throws Exception {
        // Given
        AnswerCreateRequest updateRequest = new AnswerCreateRequest(
                testQuestion.getId(), "이것은 수정된답변입니다.", "장지효", true, "https://link.com",
                "노래 제목", "가수 이름", "https://audio.url", "https://image.url", true
        );
        MockMultipartFile imageFile = new MockMultipartFile("imageFile", "image.jpg", MediaType.IMAGE_JPEG_VALUE, "image content".getBytes());
        MockMultipartFile requestFile = new MockMultipartFile("request", "", MediaType.APPLICATION_JSON_VALUE, objectMapper.writeValueAsBytes(updateRequest));

        AnswerDetailResponse answerDetailResponse = new AnswerDetailResponse(
                1L, testQuestion.getId(), testQuestion.getContent(), testMember.getId(), "이것은 수정된답변입니다.",
                testMember.getNickname(), "장지효", true, "https://link.com", "노래 제목", "가수 이름", "https://audio.url",
                "https://image.url", LocalDateTime.now()
        );

        // When
        when(answerService.updateAnswer(eq(1L), any(AnswerCreateRequest.class), any(MockMultipartFile.class)))
                .thenReturn(answerDetailResponse);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.multipart(HttpMethod.PUT, "/api/answers/{answerId}", 1L)
                        .file(imageFile)
                        .file(requestFile)
                        .header("Authorization", "Bearer " + refreshToken)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andDo(print()) // 응답 내용 출력
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("이것은 수정된답변입니다."))
                .andExpect(jsonPath("$.nickname").value("장지효"))
                .andExpect(jsonPath("$.profileOnOff").value(true))
                .andExpect(jsonPath("$.linkAttachments").value("https://link.com"))
                .andExpect(jsonPath("$.musicName").value("노래 제목"))
                .andExpect(jsonPath("$.musicSinger").value("가수 이름"))
                .andExpect(jsonPath("$.musicAudioUrl").value("https://audio.url"))
                .andExpect(jsonPath("$.imageUrl").value("https://image.url"));
    }


    @Test
    @DisplayName("답변 삭제 테스트(): 답변을 삭제한다.")
    public void deleteAnswerTest() throws Exception {
        mockMvc.perform(delete("/api/answers/{answerId}", 1L)
                        .header("Authorization", "Bearer " + refreshToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("답변 반응 확인 테스트(): 답변에 대한 반응 상태를 확인한다.")
    public void hasReactedTest() throws Exception {
        when(answerService.hasReacted(eq(1L), eq(testMember.getId()))).thenReturn(Map.of());

        mockMvc.perform(get("/api/answers/{answerId}/reacted", 1L)
                        .param("memberId", String.valueOf(testMember.getId()))
                        .header("Authorization", "Bearer " + refreshToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

//    @Test
//    @DisplayName("답변 반응 카운트 조회 테스트(): 답변의 반응 카운트를 조회한다.")
//    public void getReactionCountsTest() throws Exception {
//        when(answerService.getReactionCounts(eq(1L))).thenReturn(new ReactionResponse.CountReactionInformationDto(1, 2, 3, 4));
//
//        mockMvc.perform(get("/api/answers/{answerId}/reactionsCount", 1L)
//                        .header("Authorization", "Bearer " + refreshToken)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.heartCount").value(1))
//                .andExpect(jsonPath("$.curiousCount").value(2))
//                .andExpect(jsonPath("$.sadCount").value(3))
//                .andExpect(jsonPath("$.connectCount").value(4));
//    }
}
