package com.web.baebaeBE.global.error;

import lombok.Builder;
import lombok.Getter;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

@Getter
@Builder
public class ErrorResponse {
//    클라이언트쪽으로 반환할 ErrorResponse
    // 에러코드와 에러메시지 반환

    private String errorCode;
    private String errorMessage;

    public static ErrorResponse of(String errorCode, String errorMessage) {
        return ErrorResponse.builder()
                .errorCode(errorCode)
                .errorMessage(errorMessage)
                .build();
    }

    public static ErrorResponse of(String errorCode, BindingResult bindingResult) {
//      클라이언트에서 서버로 요청을 해서 BeanValidation을 통해 입력값 검증 시,
//      입력값에 오류가 있으면 bindingResult에 오류 정보를 넣을 수 있음
//      오류 정보를 이용해 에러메시지를 만들고, 클라이언트 쪽으로 반환할 것임
        return ErrorResponse.builder()
                .errorCode(errorCode)
                .errorMessage(createErrorMessage(bindingResult))
//              에러 메시지에는 어떤 필드에서 어떤 오류를 갖고 있는지 createErrorMessage를 통해 확인 가능
                .build();
    }

    private static String createErrorMessage(BindingResult bindingResult) {
//       StringBuilder를 이용해 bindingResult에 있던 fieldErrors를 가져오고, 에러 정보를 문자열로 붙임
        StringBuilder sb = new StringBuilder();
        boolean isFirst = true;

        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            if(!isFirst) {
                sb.append(", ");
//                첫 번째가 아니면 error메시지를 콤마(,)로 연결함
            } else {
                isFirst = false;
            }
            sb.append("[");
            sb.append(fieldError.getField());
            sb.append("] ");
            sb.append(fieldError.getDefaultMessage());
        }

        return sb.toString();
    }

}
