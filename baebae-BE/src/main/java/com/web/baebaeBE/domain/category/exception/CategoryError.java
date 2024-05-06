package com.web.baebaeBE.domain.category.exception;

import com.web.baebaeBE.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CategoryError implements ErrorCode {
  CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "C-001", "해당 카테고리를 찾을 수 없습니다.");

  private final HttpStatus httpStatus;
  private final String errorCode;
  private final String message;

}
