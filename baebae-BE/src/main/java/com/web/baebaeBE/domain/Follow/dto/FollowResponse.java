package com.web.baebaeBE.domain.Follow.dto;

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
        private boolean following;

        public static FollowMemberResponse of(Member member, boolean isFollowing) {
            return FollowMemberResponse.builder()
                    .memberId(member.getId())
                    .nickname(member.getNickname())
                    .profileImage(member.getProfileImage())
                    .following(isFollowing)
                    .build();
        }
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class isFollowingResponse {
        private boolean isFollow;

        public static isFollowingResponse of(boolean isFollow) {
            return isFollowingResponse.builder()
                    .isFollow(isFollow)
                    .build();
        }
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FollowCountResponse {
        private long followerCount;
        private long followingCount;

        public static FollowCountResponse of(long followerCount, long followingCount) {
            return FollowCountResponse.builder()
                    .followerCount(followerCount)
                    .followingCount(followingCount)
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class hasNewFollowersResponse {
        private boolean hasNewFollow;

        public static hasNewFollowersResponse of(boolean hasNewFollow) {
            return hasNewFollowersResponse.builder()
                    .hasNewFollow(hasNewFollow)
                    .build();
        }
    }
}
