package com.web.baebaeBE.presentation.notification.api;

import com.web.baebaeBE.presentation.notification.dto.NotificationResponse;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Notification", description = "알림 조회 API")
@SecurityRequirement(name = "baererAuth")
@RequestMapping("/api/notifications")
public interface NotificationApi {

    @Operation(
            summary = "유저의 모든 알림 조회",
            description = "지정된 멤버 ID에 대한 모든 알림을 조회합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @Parameter(
            in = ParameterIn.HEADER,
            name = "Authorization", required = true,
            schema = @Schema(type = "string"),
            description = "Bearer [Access 토큰]")
    @Parameter(
            in = ParameterIn.HEADER,
            name = "Authorization", required = true,
            schema = @Schema(type = "string"),
            description = "Bearer [Access 토큰]")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(
                                    implementation = NotificationResponse.NotificationListResponse.class
                            ))
            ),
            @ApiResponse(responseCode = "401", description = "토큰 인증 실패",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\n" +
                                    "  \"errorCode\": \"M-003\",\n" +
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
    ResponseEntity<NotificationResponse.NotificationListResponse> getNotificationsListByMember(@Parameter(description = "멤버의 ID", required = true) @PathVariable Long memberId);

    @Operation(
            summary = "알림 세부정보 조회",
            description = "지정된 알림 ID로 알림 세부정보를 조회합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @Parameter(
            in = ParameterIn.HEADER,
            name = "Authorization", required = true,
            schema = @Schema(type = "string"),
            description = "Bearer [Access 토큰]")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = NotificationResponse.NotificationContentResponse.class))
            ),
            @ApiResponse(responseCode = "401", description = "토큰 인증 실패",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\n" +
                                    "  \"errorCode\": \"M-003\",\n" +
                                    "  \"message\": \"해당 토큰은 유효한 토큰이 아닙니다.\"\n" +
                                    "}"))
            )
    })
    ResponseEntity<NotificationResponse.NotificationContentResponse> getNotificationById(@Parameter(description = "알림의 ID", required = true) @PathVariable Long notificationId);
}
