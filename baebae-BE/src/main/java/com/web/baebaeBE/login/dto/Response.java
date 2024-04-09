package com.web.baebaeBE.login.dto;

import com.web.baebaeBE.login.domain.Member;
import lombok.*;

public class Response {

    @Builder
    @Data
    @AllArgsConstructor
    @Getter
    public static class LoginResponse {
        private String grantType;
        private String accessToken;
        private String refreshToken;
    }

    @Getter
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class SignUpResponse {

        private Long id;
        private String username;
        private String nickname;

        static public SignUpResponse of(Member member) {
            return SignUpResponse.builder()
                    .id(member.getId())
                    .username(member.getUsername())
                    .nickname(member.getNickname())
                    .build();
        }
    }
}
