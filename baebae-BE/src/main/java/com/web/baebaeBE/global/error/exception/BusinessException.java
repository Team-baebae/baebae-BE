package com.web.baebaeBE.global.error.exception;

import com.web.baebaeBE.global.error.ErrorCode;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
//  비즈니스 로직 수행 중 예외를 발생시켜야하는 경우 사용할 Custom Exception 정의
    private ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
