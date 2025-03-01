package com.web.baebaeBE.domain.Follow.controller.api;

import com.web.baebaeBE.domain.Follow.dto.FollowResponse;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Follow", description = "Follow 관련 API")
public interface FollowApi {


    @Operation(
            summary = "팔로우 추가",
            description = "상대방을 팔로우 합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @Parameter(
            in = ParameterIn.HEADER,
            name = "Authorization", required = true,
            schema = @Schema(type = "string"),
            description = "Bearer [Access 토큰]")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "팔로우 성공"),
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
                                    "  \"errorCode\": \"FL-001\",\n" +
                                    "  \"message\": \"존재하지 않는 회원입니다.\"\n" +
                                    "}"))
            )
    })
    @RequestMapping(method = RequestMethod.POST, value = "/{followerId}/{followingId}")
    public ResponseEntity<Void> FollowMember(
            @PathVariable Long followerId,
            @PathVariable Long followingId
    );

    @Operation(
            summary = "팔로우 삭제",
            description = "상대방과의 팔로우를 취소합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @Parameter(
            in = ParameterIn.HEADER,
            name = "Authorization", required = true,
            schema = @Schema(type = "string"),
            description = "Bearer [Access 토큰]")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "팔로우 삭제 성공"),
            @ApiResponse(responseCode = "401", description = "토큰 인증 실패",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\n" +
                                    "  \"errorCode\": \"T-002\",\n" +
                                    "  \"message\": \"해당 토큰은 유효한 토큰이 아닙니다.\"\n" +
                                    "}"))
            ),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 팔로우관계",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\n" +
                                    "  \"errorCode\": \"FL-002\",\n" +
                                    "  \"message\": \"존재하지 않는 팔로우 관계 입니다.\"\n" +
                                    "}"))
            )
    })
    @RequestMapping(method = RequestMethod.DELETE, value = "/{followerId}/{followingId}")
    public ResponseEntity<Void> DeleteFollower(
            @PathVariable Long followerId,
            @PathVariable Long followingId
    );


    @Operation(
            summary = "나를 추가한 팔로워 목록 조회",
            description = "나를 팔로우하는 회원들의 목록을 조회합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @Parameter(
            in = ParameterIn.HEADER,
            name = "Authorization", required = true,
            schema = @Schema(type = "string"),
            description = "Bearer [Access 토큰]")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회"),
            @ApiResponse(responseCode = "401", description = "토큰 인증 실패",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\n" +
                                    "  \"errorCode\": \"T-002\",\n" +
                                    "  \"message\": \"해당 토큰은 유효한 토큰이 아닙니다.\"\n" +
                                    "}"))
            )
    })
    @RequestMapping(method = RequestMethod.GET, value = "/followers/{memberId}")
    public ResponseEntity<Page<FollowResponse.FollowMemberResponse>> getFollowerList(
            @PathVariable Long memberId,
            @PageableDefault(page=0, size=10, sort="createdAt", direction= Sort.Direction.DESC)
            Pageable page
    );


    @Operation(
            summary = "내가 추가한 팔로잉 목록 조회",
            description = "내가 팔로우 하는 회원들의 목록을 조회합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @Parameter(
            in = ParameterIn.HEADER,
            name = "Authorization", required = true,
            schema = @Schema(type = "string"),
            description = "Bearer [Access 토큰]")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회"),
            @ApiResponse(responseCode = "401", description = "토큰 인증 실패",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\n" +
                                    "  \"errorCode\": \"T-002\",\n" +
                                    "  \"message\": \"해당 토큰은 유효한 토큰이 아닙니다.\"\n" +
                                    "}"))
            )
    })
    @RequestMapping(method = RequestMethod.GET, value = "/followings/{memberId}")
    public ResponseEntity<Page<FollowResponse.FollowMemberResponse>> getFollowingList(
            @PathVariable Long memberId,
            @Parameter(hidden = false)
            @PageableDefault(page=0, size=10, sort="createdAt", direction= Sort.Direction.DESC)
            Pageable page
    );

    @Operation(
            summary = "팔로우 여부 조회",
            description = "팔로우 여부를 조회합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @Parameter(
            in = ParameterIn.HEADER,
            name = "Authorization", required = true,
            schema = @Schema(type = "string"),
            description = "Bearer [Access 토큰]")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회"),
            @ApiResponse(responseCode = "401", description = "토큰 인증 실패",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\n" +
                                    "  \"errorCode\": \"T-002\",\n" +
                                    "  \"message\": \"해당 토큰은 유효한 토큰이 아닙니다.\"\n" +
                                    "}"))
            )
    })
    @GetMapping("isFollowing/{followerId}/{followingId}")
    public ResponseEntity<FollowResponse.isFollowingResponse> isFollowing(
            @PathVariable Long followerId,
            @PathVariable Long followingId
    );



}
