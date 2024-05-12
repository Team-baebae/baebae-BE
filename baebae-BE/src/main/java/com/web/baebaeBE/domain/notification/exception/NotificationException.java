package com.web.baebaeBE.domain.notification.exception;

import com.web.baebaeBE.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum NotificationException implements ErrorCode {


    NOT_EXIST_MEMBER(HttpStatus.NOT_FOUND, "A-001", "존재하지 않는 알림입니다.");

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;

}
