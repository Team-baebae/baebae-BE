package com.web.baebaeBE.login.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.web.baebaeBE.login.domain.Member;
import com.web.baebaeBE.login.domain.MemberType;
import lombok.*;

import java.util.Date;

/**
 * 로그인 시 컨트롤러에서 사용할 response, request dto 작성
 */
public class LoginResponse {

    @Getter @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SignUp {
        private Long id;
        private String email;
        private String nickname;
        private MemberType memberType;
        private String refreshToken;
        private String accessToken;


        public static SignUp of(Member member, String accessToken) {
            return SignUp.builder()
                    .id(member.getId())
                    .email(member.getEmail())
                    .nickname(member.getNickname())
                    .memberType(member.getMemberType())
                    .refreshToken(member.getRefreshToken())
                    .accessToken(accessToken)
                    .build();
        }

    }


}
