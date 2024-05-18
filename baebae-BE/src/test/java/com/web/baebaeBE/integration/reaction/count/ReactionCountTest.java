package com.web.baebaeBE.integration.reaction.count;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class ReactionCountTest {

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
