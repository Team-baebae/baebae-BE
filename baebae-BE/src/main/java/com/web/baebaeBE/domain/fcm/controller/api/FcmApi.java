package com.web.baebaeBE.domain.fcm.controller.api;

import com.web.baebaeBE.domain.fcm.dto.FcmRequest;
import com.web.baebaeBE.domain.fcm.entity.FcmToken;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Tag(name = "Fcm", description = "FCM 관련 API")
public interface FcmApi {

    @Operation(
            summary = "FCM 토큰 추가",
            description = "회원의 FCM 토큰을 추가합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @Parameter(
            in = ParameterIn.HEADER,
            name = "Authorization", required = true,
            schema = @Schema(type = "string"),
            description = "Bearer [Access 토큰]")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "업데이트 성공"),
            @ApiResponse(responseCode = "401", description = "토큰 인증 실패",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\n" +
                                    "  \"errorCode\": \"T-002\",\n" +
                                    "  \"message\": \"해당 토큰은 유효한 토큰이 아닙니다.\"\n" +
                                    "}"))
            ),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 회원",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\n" +
                                    "  \"errorCode\": \"M-002\",\n" +
                                    "  \"message\": \"존재하지 않는 회원입니다.\"\n" +
                                    "}"))
            )
    })
    @RequestMapping(method = RequestMethod.POST, value = "/{memberId}")
    ResponseEntity<Void> addFcmToken(@PathVariable Long memberId,
                                     @RequestBody FcmRequest.Token request);
}
