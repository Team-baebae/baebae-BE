package com.web.baebaeBE.presentation.kakao.api;


import com.web.baebaeBE.presentation.kakao.dto.KakaoDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Kakao", description = "카카오 토큰 생성 API")
public interface KakaoApi {

    @Operation(summary = "카카오 로그인 콜백 처리",
            description = "인증 코드를 받아 카카오 서버로부터 토큰을 요청하고 응답합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공적으로 토큰을 받음",
                            content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = KakaoDto.Response.class))),
                    @ApiResponse(responseCode = "401", description = "승인 코드 오류",
                            content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\n" +
                                    "  \"errorCode\": \"K-002\",\n" +
                                    "  \"message\": \"유효하지않은 카카오 승인 코드 입니다.\"\n" +
                                    "}"))
                    )
            })
    ResponseEntity<KakaoDto.Response> loginCallback(
            @Parameter(description = "카카오 서버로부터 받은 인증 코드", required = true) @RequestParam("code") String code,
            @Parameter(description = "리다이렉트 URI", required = false) @RequestParam(value = "redirectUri", required = false) String redirectUri);
}