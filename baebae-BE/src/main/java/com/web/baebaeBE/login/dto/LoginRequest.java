package com.web.baebaeBE.login.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.web.baebaeBE.login.domain.MemberType;
import lombok.*;

import java.util.Date;

/**
 * 로그인 시 컨트롤러에서 사용할 response, request dto 작성
 */
public class LoginRequest {

    @Getter
    @Setter
    @NoArgsConstructor
    public static class SignUp {
        private MemberType memberType;
        private String nickname;
    }

}
