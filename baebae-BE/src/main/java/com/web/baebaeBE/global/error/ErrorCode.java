package com.web.baebaeBE.global.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;


// 공통 ErrorCode 선언
public interface ErrorCode {

    HttpStatus getHttpStatus();

    String getErrorCode();

    String getMessage();


}
