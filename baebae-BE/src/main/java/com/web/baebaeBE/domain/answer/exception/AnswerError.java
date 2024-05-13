package com.web.baebaeBE.domain.answer.exception;

import com.web.baebaeBE.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AnswerError implements ErrorCode {

    NO_EXIST_MEMBER(HttpStatus.NOT_FOUND, "M-001", "존재하지 않는 회원입니다."),
    NO_EXIST_ANSWER(HttpStatus.NOT_FOUND, "M-002", "존재하지 않는 답변입니다."),
    NO_EXIST_QUESTION(HttpStatus.NOT_FOUND, "M-003", "존재하지 않는 질문입니다."),
    IMAGE_PROCESSING_ERROR(HttpStatus.NOT_FOUND, "M-004", "이미지 업로드 오류입니다.");

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;
}
