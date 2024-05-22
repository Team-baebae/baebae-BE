package com.web.baebaeBE.integration.reaction.count;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.baebaeBE.domain.member.entity.Member;
import com.web.baebaeBE.domain.member.entity.MemberType;
import com.web.baebaeBE.domain.member.repository.MemberRepository;
import com.web.baebaeBE.domain.oauth2.controller.Oauth2Controller;
import com.web.baebaeBE.domain.question.dto.QuestionDetailResponse;
import com.web.baebaeBE.domain.question.repository.QuestionRepository;
import com.web.baebaeBE.domain.question.service.QuestionService;
import com.web.baebaeBE.domain.reactioncount.dto.ReactionResponse;
import com.web.baebaeBE.domain.reactioncount.service.ReactionService;
import com.web.baebaeBE.global.jwt.JwtTokenProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Duration;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
public class ReactionCountTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private MemberRepository memberRepository;
    @MockBean
    private QuestionRepository questionRepository;
    @MockBean
    private ReactionService reactionService;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    private Oauth2Controller oauth2Controller;
    private Member testMember;
    private Member testReceiver;
    private String refreshToken;
    private QuestionDetailResponse testQuestionDetailResponse;

    @BeforeEach
    void setup() {
        testMember = Member.builder()
                .email("test@gmail.com")
                .nickname("장지효")
                .memberType(MemberType.KAKAO)
                .refreshToken("null")
                .build();

        testReceiver = Member.builder()
                .email("test@gmail2.com")
                .nickname("장지효2")
                .memberType(MemberType.KAKAO)
                .refreshToken("null")
                .build();

        refreshToken = tokenProvider.generateToken(testMember, Duration.ofDays(14));
        testMember.updateRefreshToken(refreshToken);
        memberRepository.save(testMember);

        refreshToken = tokenProvider.generateToken(testReceiver, Duration.ofDays(14));
        testReceiver.updateRefreshToken(refreshToken);
        memberRepository.save(testReceiver);

        when(memberRepository.findByEmail("test@gmail.com")).thenReturn(Optional.of(testMember));
        when(memberRepository.findByEmail("test@gmail2.com")).thenReturn(Optional.of(testReceiver));

    }

    @AfterEach
    void tearDown() {
        Optional<Member> member = memberRepository.findByEmail("test@gmail.com");
        member.ifPresent(memberRepository::delete);
    }


    @Test
    @DisplayName("답변 반응 카운트 조회 테스트(): 답변의 반응 카운트를 조회한다.")
    public void getReactionCountsTest() throws Exception {
        when(reactionService.getReactionCounts(eq(1L))).thenReturn(new ReactionResponse.CountReactionInformationDto(1, 2, 3, 4));

        mockMvc.perform(get("/api/reactionsCount/{answerId}/reactionsCount", 1L)
                        .header("Authorization", "Bearer " + refreshToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.heartCount").value(1))
                .andExpect(jsonPath("$.curiousCount").value(2))
                .andExpect(jsonPath("$.sadCount").value(3))
                .andExpect(jsonPath("$.connectCount").value(4));
    }
}
