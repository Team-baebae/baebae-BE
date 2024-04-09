package com.web.baebaeBE.login.dto;

import com.web.baebaeBE.login.domain.Member;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

public class Request {

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    public static class LoginRequest {
        private String username;
        private String password;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SignUpRequest {

        private String username;
        private String password;
        private String nickname;
        private List<String> roles = new ArrayList<>();

        public Member toEntity(String encodedPassword, List<String> roles) {

            return Member.builder()
                    .username(username)
                    .password(encodedPassword)
                    .nickname(nickname)
                    .roles(roles)
                    .build();
        }
    }
}
