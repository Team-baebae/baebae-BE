package com.web.baebaeBE.domain.reactioncount.dto;

import com.web.baebaeBE.domain.reactioncount.entity.ReactionCount;
import lombok.*;

public class ReactionResponse {

    @Getter
    @AllArgsConstructor
    public static class CountReactionInformationDto {
        private int heartCount;
        private int curiousCount;
        private int sadCount;
        private int connectCount;

        public static CountReactionInformationDto of(ReactionCount reactionCount) {
            return new CountReactionInformationDto(
                    reactionCount.getHeartCount(),
                    reactionCount.getCuriousCount(),
                    reactionCount.getSadCount(),
                    reactionCount.getConnectCount()
            );
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
        private int connectCount;

        public static ReactionInformationDto of(ReactionCount reactionCount, boolean isClicked) {
            return ReactionInformationDto.builder()
                    .isClicked(isClicked)
                    .heartCount(reactionCount.getHeartCount())
                    .curiousCount(reactionCount.getCuriousCount())
                    .sadCount(reactionCount.getSadCount())
                    .connectCount(reactionCount.getConnectCount())
                    .build();
        }
    }
}
