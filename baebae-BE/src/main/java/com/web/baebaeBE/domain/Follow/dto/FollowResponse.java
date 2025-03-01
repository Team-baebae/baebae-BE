package com.web.baebaeBE.domain.Follow.dto;

import com.web.baebaeBE.domain.Follow.entity.Follow;
import com.web.baebaeBE.domain.member.dto.MemberResponse;
import com.web.baebaeBE.domain.member.entity.Member;
import lombok.*;

public class FollowResponse {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FollowMemberResponse {
        private Long memberId;
        private String nickname;
        private String profileImage;

        public static FollowMemberResponse of(Member member) {
            return FollowMemberResponse.builder()
                    .memberId(member.getId())
                    .nickname(member.getNickname())
                    .profileImage(member.getProfileImage())
                    .build();
        }
    }
}
