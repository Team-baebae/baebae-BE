package com.web.baebaeBE.global.jwt.exception;

import com.web.baebaeBE.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;


@Getter
@RequiredArgsConstructor
public enum TokenError implements ErrorCode {

    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "T-001", "해당 토큰이 만료되었습니다."),
    NOT_VALID_TOKEN(HttpStatus.UNAUTHORIZED, "T-002", "해당 토큰은 유효한 토큰이 아닙니다."),
    NOT_EXISTS_AUTHORIZATION(HttpStatus.UNAUTHORIZED, "T-003", "Authorization Header가 빈값입니다."),
    NOT_VALID_BEARER_GRANT_TYPE(HttpStatus.UNAUTHORIZED, "T-004", "인증 타입이 Bearer 타입이 아닙니다.");


    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;
}
