package com.web.baebaeBE.domain23.question.exception;

import com.web.baebaeBE.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum QuestionError implements ErrorCode {
    NO_EXIST_MEMBER(HttpStatus.NOT_FOUND, "M-001", "존재하지 않는 회원입니다."),
    UNFILLED_QUESTION(HttpStatus.NOT_FOUND, "M-002", "빈칸으로 제출할 수 없습니다."),
    NO_EXIST_QUESTION(HttpStatus.NOT_FOUND, "M-003", "존재하지 않는 질문입니다.");

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;
}