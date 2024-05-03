package com.web.baebaeBE.presentation.member.api;


import com.web.baebaeBE.presentation.member.dto.MemberRequest;
import com.web.baebaeBE.presentation.member.dto.MemberResponse;
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

@Tag(name = "Login", description = "로그인 관련 API")
public interface MemberApi {

    @Operation(
            summary = "로그인",
            description = "기존 회원일 경우 로그인, 새로운 회원일 경우 회원가입을 진행합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @Parameter(
            in = ParameterIn.HEADER,
            name = "Authorization", required = true,
            schema = @Schema(type = "string"),
            description = "Bearer [카카오 Access 토큰]")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = MemberResponse.SignUpResponse.class))
            ),
            @ApiResponse(responseCode = "400", description = "회원가입 실패",
                content = @Content(mediaType = "application/json",
                examples = @ExampleObject(value = "{\n" +
                        "  \"errorCode\": \"M-003\",\n" +
                        "  \"message\": \"초기회원가입 데이터가 필요합니다.\"\n" +
                        "}"))
            ),
            @ApiResponse(responseCode = "401", description = "토큰 인증 실패",
                content = @Content(mediaType = "application/json",
                examples = @ExampleObject(value = "{\n" +
                        "  \"errorCode\": \"M-002\",\n" +
                        "  \"message\": \"해당 토큰은 유효한 토큰이 아닙니다.\"\n" +
                        "}"))
            ),
    })
    ResponseEntity< MemberResponse.SignUpResponse> oauthSignUp(
            MemberRequest.SignUp signUpRequest,
            HttpServletRequest httpServletRequest
    );

    @Operation(
            summary = "회원가입 유무 체크",
            description = "카카오 토큰을 기반으로 회원가입 유무를 체크합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @Parameter(
            in = ParameterIn.HEADER,
            name = "Authorization", required = true,
            schema = @Schema(type = "string"),
            description = "Bearer [카카오 Access 토큰]")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "기존 회원",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\n" +
                                    "  \"isExisting\": \"true\"\n" +
                                    "}"))
            ),
            @ApiResponse(responseCode = "200", description = "새로운 회원",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\n" +
                                    "  \"isExisting\": \"false\"\n" +
                                    "}"))
            )
    })
    public ResponseEntity<MemberResponse.isExistingUserResponse> isExistingUser(
            HttpServletRequest httpServletRequest
    );



    @Operation(
            summary = "Access Token 재발급",
            description = "Refresh Token을 기반으로, 새로운 Access Token을 발급합니다."
    )
    @Parameter(
            in = ParameterIn.HEADER,
            name = "Authorization", required = true,
            schema = @Schema(type = "string"),
            description = "Bearer [Refresh 토큰]")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "재발급 성공",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = MemberResponse.AccessTokenResponse.class))
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
    ResponseEntity<MemberResponse.AccessTokenResponse> refreshToken(
            HttpServletRequest httpServletRequest);



    @Operation(
            summary = "로그아웃",
            description = "Refresh Token 만료시간을 현재시간으로 설정해 로그아웃 시킵니다."
    )
    @Parameter(
            in = ParameterIn.HEADER,
            name = "Authorization", required = true,
            schema = @Schema(type = "string"),
            description = "Bearer [Access 토큰]")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그아웃 성공"),
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
    ResponseEntity<Void> logout(HttpServletRequest httpServletRequest);
}
