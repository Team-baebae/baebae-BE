package com.web.baebaeBE.presentation.manage.member.api;

import com.web.baebaeBE.presentation.manage.member.dto.ManageMemberRequest;
import com.web.baebaeBE.presentation.manage.member.dto.ManageMemberResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Member", description = "회원 관리 API")
public interface ManageMemberApi {

    @Operation(
            summary = "회원 정보 조회",
            description = "회원의 정보를 조회합니다.",
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
                            schema = @Schema(implementation = ManageMemberResponse.MemberInformationResponse.class))),
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
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    ResponseEntity<ManageMemberResponse.MemberInformationResponse> getMemberInformation(@PathVariable Long id);


    @Operation(
            summary = "회원 id 조회",
            description = "주어진 회원의 닉네임 정보를 바탕으로 회원의 id를 조회합니다. ",
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
                            schema = @Schema(implementation = Long.class))),
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
    @GetMapping("/members/nickname/{nickname}")
    Long getMemberIdByNickname(@PathVariable String nickname);

    @Operation(
            summary = "프로필 사진 업데이트",
            description = "회원의 프로필 사진을 업데이트합니다.",
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
    @RequestMapping(method = RequestMethod.PATCH, value = "/profile-image/{id}")
    ResponseEntity<ManageMemberResponse.ObjectUrlResponse> updateProfileImage(@PathVariable Long id,
                                            @RequestPart("image") MultipartFile image);




    @Operation(
            summary = "FCM 토큰 업데이트",
            description = "회원의 FCM 토큰을 업데이트합니다.",
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
    @RequestMapping(method = RequestMethod.PATCH, value = "/fcm-token/{id}")
    ResponseEntity<Void> updateFcmToken(@PathVariable Long id,
                                        @RequestBody ManageMemberRequest.UpdateFcmTokenDto updateFcmTokenDto);




    @Operation(
            summary = "닉네임 수정",
            description = "회원의 닉네임을 수정합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @Parameter(
            in = ParameterIn.HEADER,
            name = "Authorization", required = true,
            schema = @Schema(type = "string"),
            description = "Bearer [Access 토큰]")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수정 성공"),
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
    @RequestMapping(method = RequestMethod.PATCH, value = "/nickname/{id}")
    ResponseEntity<Void> updateNickname(@PathVariable Long id,
                                        @RequestBody ManageMemberRequest.UpdateNicknameDto updateNicknameDto);

    @Operation(
            summary = "회원탈퇴",
            description = "해당 회원의 정보를 영구 삭제합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @Parameter(
            in = ParameterIn.HEADER,
            name = "Authorization", required = true,
            schema = @Schema(type = "string"),
            description = "Bearer [Access 토큰]")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "탈퇴 성공"),
            @ApiResponse(responseCode = "401", description = "토큰 인증 실패",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\n" +
                                    "  \"errorCode\": \"MM-002\",\n" +
                                    "  \"message\": \"회원정보와 토큰정보가 일치하지 않습니다.\"\n" +
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
    ResponseEntity<Void> deleteMember(@PathVariable Long memberId,
                                        HttpServletRequest httpServletRequest);

}