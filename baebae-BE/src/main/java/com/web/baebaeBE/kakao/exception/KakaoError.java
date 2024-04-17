package com.web.baebaeBE.kakao.exception;

import com.web.baebaeBE.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum KakaoError implements ErrorCode {

    NOT_FOUND_KAKAO_INFO(HttpStatus.NOT_FOUND,"K-001", "해당 유저의 카카오 정보를 가져올 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;

}
