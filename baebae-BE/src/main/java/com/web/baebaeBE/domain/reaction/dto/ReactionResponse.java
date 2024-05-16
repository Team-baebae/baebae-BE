package com.web.baebaeBE.domain.reaction.dto;

import com.web.baebaeBE.domain.answer.entity.Answer;
import com.web.baebaeBE.domain.reaction.entity.MemberAnswerReaction;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.*;

public class ReactionResponse {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CountReactionInformationDto {
        private Integer heartCount;
        private Integer curiousCount;
        private Integer sadCount;
        private Integer connectCount;

        public static CountReactionInformationDto of(Answer answer) {
            return CountReactionInformationDto.builder()
                    .heartCount(answer.getHeartCount())
                    .curiousCount(answer.getCuriousCount())
                    .sadCount(answer.getSadCount())
                    .connectCount(answer.getConnectCount())
                    .build();
        }
    }
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReactionInformationDto {
        private boolean isClicked;
        private int heartCount;
        private int curiousCount;
        private int sadCount;

        public static ReactionInformationDto of(Answer answer, boolean isClicked) {
            return ReactionInformationDto.builder()
                    .isClicked(isClicked)
                    .heartCount(answer.getHeartCount())
                    .curiousCount(answer.getCuriousCount())
                    .sadCount(answer.getSadCount())
                    .build();
        }
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ConnectionReactionInformationDto {
        private boolean isClicked;
        private boolean isMatched;

        public static ConnectionReactionInformationDto of(boolean isClicked, boolean isMatched) {
            return ConnectionReactionInformationDto.builder()
                    .isClicked(isClicked)
                    .isMatched(isMatched)
                    .build();
        }
    }
}
