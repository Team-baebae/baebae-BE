package com.web.baebaeBE.integration.question;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.baebaeBE.infra.member.entity.Member;
import com.web.baebaeBE.infra.member.enums.MemberType;
import com.web.baebaeBE.infra.member.repository.MemberRepository;
import com.web.baebaeBE.infra.question.entity.Question;
import com.web.baebaeBE.infra.question.repository.QuestionRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
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
    private final ObjectMapper objectMapper = new ObjectMapper();
    private Member testMember;

    @BeforeEach
    void setup() {
        testMember = memberRepository.save(Member.builder()
                .email("test@gmail.com")
                .nickname("장지효")
                .memberType(MemberType.KAKAO)
                .refreshToken("null")
                .build());
    }

    @AfterEach
    void tearDown() {
        Optional<Member> member = memberRepository.findByEmail("test@gmail.com");
        member.ifPresent(memberRepository::delete);
    }

    @Test
    @DisplayName("질문 생성 테스트(): 질문을 생성한다.")
    public void createQuestionTest() throws Exception {
        // given
        String content = "이것은 질문입니다.";

        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/api/question")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new Question(null, testMember, content, "닉네임", true, LocalDateTime.now()))))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value(content));
    }

    @Test
    @DisplayName("회원별 질문 조회 테스트(): 해당 회원의 질문을 조회한다.")
    public void getQuestionsByMemberIdTest() throws Exception {

        String content = "이것은 회원의 질문입니다.";
        Question question = questionRepository.save(new Question(null, testMember, content, "닉네임", true, LocalDateTime.now()));


        mockMvc.perform(MockMvcRequestBuilders.get("/api/question/member/{memberId}", testMember.getId())
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value(content));
    }

    @Test
    @DisplayName("질문 수정 테스트(): 질문을 수정한다.")
    public void updateQuestionTest() throws Exception {

        String content = "이것은 수정 전의 질문입니다.";
        Question question = questionRepository.save(new Question(null, testMember, content, "닉네임", true, LocalDateTime.now()));
        String updatedContent = "이것은 수정 후의 질문입니다.";


        mockMvc.perform(MockMvcRequestBuilders.put("/api/question/{questionId}", question.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedContent))

                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("질문 삭제 테스트(): 질문을 삭제한다.")
    public void deleteQuestionTest() throws Exception {

        String content = "이것은 삭제할 질문입니다.";
        Question question = questionRepository.save(new Question(null, testMember, content, "닉네임", true, LocalDateTime.now()));

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/question/{questionId}", question.getId())
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk());
    }
}
