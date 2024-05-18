package com.web.baebaeBE.domain.answer.controller.api;

import com.web.baebaeBE.domain.answer.dto.AnswerCreateRequest;
import com.web.baebaeBE.domain.answer.dto.AnswerDetailResponse;
import com.web.baebaeBE.domain.answer.dto.AnswerResponse;
import com.web.baebaeBE.domain.reaction.entity.ReactionValue;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@SecurityRequirement(name = "baererAuth")
@Tag(name = "Answer", description = "답변 관리 API")
public interface AnswerApi {

    @Operation(
            summary = "답변 생성",
            description = "새로운 답변을 생성합니다. 이미지 파일을 포함합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @Parameter(
            in = ParameterIn.HEADER,
            name = "Authorization", required = true,
            schema = @Schema(type = "string"),
            description = "Bearer [Access 토큰]")
    @ApiResponse(responseCode = "201", description = "답변 생성 성공",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = AnswerDetailResponse.class)))
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<AnswerDetailResponse> createAnswer(
            @PathVariable Long memberId,
            @RequestPart(value = "imageFiles") MultipartFile imageFile,
            @RequestPart(name = "request") AnswerCreateRequest request);

    @Operation(
            summary = "모든 답변 리스트 조회",
            description = "해당 회원의 전체 답변을 간략하게 조회합니다."
    )
    @Parameter(
            in = ParameterIn.HEADER,
            name = "Authorization", required = true,
            schema = @Schema(type = "string"),
            description = "Bearer [Access 토큰]")
    @ApiResponse(responseCode = "200", description = "답변 조회 성공",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Page.class)))
    @RequestMapping(method = RequestMethod.GET)
    ResponseEntity<List<AnswerResponse>> getAnswersByMemberId(
            @PathVariable Long memberId);
    @Operation(
            summary = "모든 답변 조회",
            description = "모든 답변을 페이지네이션으로 조회합니다."
    )
    @ApiResponse(responseCode = "200", description = "답변 조회 성공",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Page.class)))
    @GetMapping
    ResponseEntity<Page<AnswerDetailResponse>> getAllAnswers(
            @RequestParam Long memberId,
            @RequestParam(required = false) Long category,
            Pageable pageable);
    @Operation(
            summary = "답변 수정",
            description = "기존 답변을 수정합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @Parameter(
            in = ParameterIn.HEADER,
            name = "Authorization", required = true,
            schema = @Schema(type = "string"),
            description = "Bearer [Access 토큰]")
    @ApiResponse(responseCode = "201", description = "답변 수정 성공",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = AnswerDetailResponse.class)))
    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<AnswerDetailResponse> updateAnswer(
            @PathVariable Long answerId,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile,
            @RequestPart AnswerCreateRequest request);

    @Operation(
            summary = "답변 삭제",
            description = "특정 답변을 삭제합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @Parameter(
            in = ParameterIn.HEADER,
            name = "Authorization", required = true,
            schema = @Schema(type = "string"),
            description = "Bearer [Access 토큰]")
    @ApiResponse(responseCode = "204", description = "답변 삭제 성공")
    @RequestMapping(method = RequestMethod.DELETE, value = "/{answerId}")
    ResponseEntity<Void> deleteAnswer(
            @PathVariable Long answerId);


    @Operation(
            summary = "특정 답변에 대한 사용자의 반응 여부 확인",
            description = "특정 답변에 대해 사용자가 이미 반응(좋아요, 궁금해요, 슬퍼요, 통했당)을 남겼는지 여부를 확인합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @Parameter(
            in = ParameterIn.HEADER,
            name = "Authorization", required = true,
            schema = @Schema(type = "string"),
            description = "Bearer [Access 토큰]")
    @ApiResponse(responseCode = "200", description = "반응 여부 확인 성공",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Map.class)))
    @GetMapping("/{answerId}/reacted")
    ResponseEntity<Map<ReactionValue, Boolean>> hasReacted(
            @PathVariable Long answerId,
            @RequestParam Long memberId);


}
