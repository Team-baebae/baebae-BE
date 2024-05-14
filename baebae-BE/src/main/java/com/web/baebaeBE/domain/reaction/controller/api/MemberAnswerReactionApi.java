package com.web.baebaeBE.domain.reaction.controller.api;

import com.web.baebaeBE.domain.reaction.dto.ReactionRequest;
import com.web.baebaeBE.domain.reaction.dto.ReactionResponse;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestParam;


@Tag(name = "Reaction", description = "피드 반응 API")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/reactions")
public interface MemberAnswerReactionApi {

    @Operation(
            summary = "피드 반응 생성",
            description = "피드에 대한 지정된 멤버 ID와 답변 ID에 대한 반응을 생성합니다. " +
                    "(HEART, CURIOUS, SAD 만 가능합니다.)",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @Parameter(
            in = ParameterIn.HEADER,
            name = "Authorization", required = true,
            schema = @Schema(type = "string"),
            description = "Bearer [Access 토큰]")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "생성 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(
                                    implementation = ReactionResponse.ReactionInformationDto.class
                            ))
            ),
            @ApiResponse(responseCode = "401", description = "토큰 인증 실패",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\n" +
                                    "  \"errorCode\": \"M-003\",\n" +
                                    "  \"message\": \"해당 토큰은 유효한 토큰이 아닙니다.\"\n" +
                                    "}"))
            ),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 회원 또는 답변",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\n" +
                                    "  \"errorCode\": \"M-002\",\n" +
                                    "  \"message\": \"존재하지 않는 회원 또는 답변입니다.\"\n" +
                                    "}"))
            )
    })
    ResponseEntity<ReactionResponse.ReactionInformationDto> createReaction(@Parameter(description = "멤버의 ID", required = true) @PathVariable Long memberId,
                                                                           @Parameter(description = "답변의 ID", required = true) @PathVariable Long answerId,
                                                                           @RequestBody ReactionRequest.create reactionDto);

    @Operation(
            summary = "통했당 생성",
            description = "지정된 멤버 ID, 답변 ID, 대상 멤버 ID에 대한 '통했당'을 생성합니다. " +
                    "다른 피드에 '통했당'을 남길경우 memberId, answerId만 필요합니다. " +
                    "또한 피드 주인이 통했당을 완료할 경우 destinationMemberId가 추가적으로 필요합니다." +
                    "(",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @Parameter(
            in = ParameterIn.HEADER,
            name = "Authorization", required = true,
            schema = @Schema(type = "string"),
            description = "Bearer [Access 토큰]")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "생성 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(
                                    implementation = ReactionResponse.ConnectionReactionInformationDto.class
                            ))
            ),
            @ApiResponse(responseCode = "401", description = "토큰 인증 실패",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\n" +
                                    "  \"errorCode\": \"M-003\",\n" +
                                    "  \"message\": \"해당 토큰은 유효한 토큰이 아닙니다.\"\n" +
                                    "}"))
            ),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 회원 또는 답변",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\n" +
                                    "  \"errorCode\": \"M-002\",\n" +
                                    "  \"message\": \"존재하지 않는 회원 또는 답변입니다.\"\n" +
                                    "}"))
            )
    })
    ResponseEntity<ReactionResponse.ConnectionReactionInformationDto> createClickReaction(@Parameter(description = "멤버의 ID", required = true) @PathVariable Long memberId,
                                                                                          @Parameter(description = "답변의 ID", required = true) @PathVariable Long answerId,
                                                                                          @Parameter(description = "대상 멤버의 ID (피드주인이 통했당 완료할때만 가능)", required = false) @RequestParam(required = false) Long destinationMemberId);
}
