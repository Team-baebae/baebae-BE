package com.web.baebaeBE.domain.reactioncount.controller.api;

import com.web.baebaeBE.domain.reactioncount.dto.ReactionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "ReactionCount", description = "반응 수 API")
public interface ReactionCountApi {

    @Operation(
            summary = "특정 답변의 반응 개수 업데이트",
            description = "특정 답변에 대한 하트, 궁금해요, 슬퍼요, 통했당 반응의 개수를 업데이트합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponse(responseCode = "200", description = "반응 개수 업데이트 성공")
    @ApiResponse(responseCode = "401", description = "토큰 인증 실패",
            content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = "{\n" +
                            "  \"errorCode\": \"T-002\",\n" +
                            "  \"message\": \"해당 토큰은 유효한 토큰이 아닙니다.\"\n" +
                            "}")))
    @PutMapping("/{answerId}")
    ResponseEntity<Void> updateReactionCounts(@PathVariable Long answerId,
                                              @RequestParam int heartCount,
                                              @RequestParam int curiousCount,
                                              @RequestParam int sadCount,
                                              @RequestParam int connectCount);

    @Operation(
            summary = "특정 답변의 반응 개수 조회",
            description = "특정 답변에 대한 하트, 궁금해요, 슬퍼요, 통했당 반응의 개수를 조회합니다."
    )

    @ApiResponse(responseCode = "200", description = "반응 개수 조회 성공",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ReactionResponse.CountReactionInformationDto.class)))
    @GetMapping("/{answerId}/reactionsCount")
    ResponseEntity<ReactionResponse.CountReactionInformationDto> getReactionCounts(
            @PathVariable Long answerId);

}
