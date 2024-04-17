package com.web.baebaeBE.login.exception;

import com.web.baebaeBE.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;


@Getter
@RequiredArgsConstructor
public enum MemberError implements ErrorCode {

    DUPLICATE_MEMBER(HttpStatus.BAD_REQUEST,"M-001", "이미 존재하는 회원입니다."),
    NOT_EXIST_MEMBER(HttpStatus.NOT_FOUND,"M-002", "존재하지 않는 회원입니다."),
    NOT_EXIST_DATA(HttpStatus.BAD_REQUEST,"M-003", "초기회원가입 데이터가 필요합니다.");

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;

}
