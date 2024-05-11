package com.web.baebaeBE.presentation.question.api;

import com.web.baebaeBE.presentation.question.dto.QuestionCreateRequest;
import com.web.baebaeBE.presentation.question.dto.QuestionDetailResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Question", description = "질문 관리 API")
public interface QuestionApi {

    @Operation(
            summary = "질문 생성",
            description = "새로운 질문을 생성합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @Parameter(
            in = ParameterIn.HEADER,
            name = "Authorization", required = true,
            schema = @Schema(type = "string"),
            description = "Bearer [Access 토큰]")
    @ApiResponse(responseCode = "201", description = "질문 생성 성공",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = QuestionDetailResponse.class)))
    @ApiResponse(responseCode = "401", description = "토큰 인증 실패",
            content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = "{\n" +
                            "  \"errorCode\": \"T-002\",\n" +
                            "  \"message\": \"해당 토큰은 유효한 토큰이 아닙니다.\"\n" +
                            "}")))
    @RequestMapping(method = RequestMethod.POST, value = "/member/{memberId}")
    ResponseEntity<QuestionDetailResponse> createQuestion(
            @RequestBody QuestionCreateRequest questionDTO,
            @PathVariable Long memberId);

//    @Operation(
//            summary = "모든 질문 조회",
//            description = "모든 질문을 페이지네이션으로 조회합니다.",
//            security = @SecurityRequirement(name = "bearerAuth")
//    )
//    @Parameter(
//            in = ParameterIn.HEADER,
//            name = "Authorization", required = true,
//            schema = @Schema(type = "string"),
//            description = "Bearer [Access 토큰]")
//    @ApiResponse(responseCode = "200", description = "질문 조회 성공",
//            content = @Content(mediaType = "application/json",
//                    schema = @Schema(implementation = QuestionDetailResponse.class)))
//    @RequestMapping(method = RequestMethod.GET)
//    ResponseEntity<List<QuestionDetailResponse>> getAllQuestions(
//            @RequestParam Long memberId,
//            Pageable pageable);

    @Operation(
            summary = "답변된 질문 조회",
            description = "답변된 모든 질문을 페이지네이션으로 조회합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @Parameter(
            in = ParameterIn.HEADER,
            name = "Authorization", required = true,
            schema = @Schema(type = "string"),
            description = "Bearer [Access 토큰]")
    @ApiResponse(responseCode = "200", description = "답변된 질문 조회 성공",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = QuestionDetailResponse.class)))
    @GetMapping("/answered/{memberId}")
    ResponseEntity<List<QuestionDetailResponse>> getAnsweredQuestions(
            @PathVariable Long memberId, Pageable pageable);

    @Operation(
            summary = "답변되지 않은 질문 조회",
            description = "답변되지 않은 모든 질문을 페이지네이션으로 조회합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @Parameter(
            in = ParameterIn.HEADER,
            name = "Authorization", required = true,
            schema = @Schema(type = "string"),
            description = "Bearer [Access 토큰]")
    @ApiResponse(responseCode = "200", description = "답변되지 않은 질문 조회 성공",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = QuestionDetailResponse.class)))
    @GetMapping("/unanswered/{memberId}")
    ResponseEntity<List<QuestionDetailResponse>> getUnansweredQuestions(
            @PathVariable Long memberId, Pageable pageable);

    @Operation(
            summary = "질문 수정",
            description = "기존 질문을 수정합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @Parameter(
            in = ParameterIn.HEADER,
            name = "Authorization", required = true,
            schema = @Schema(type = "string"),
            description = "Bearer [Access 토큰]")
    @ApiResponse(responseCode = "204", description = "질문 수정 성공")

    @ApiResponse(responseCode = "401", description = "토큰 인증 실패",
            content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = "{\n" +
                            "  \"errorCode\": \"T-002\",\n" +
                            "  \"message\": \"해당 토큰은 유효한 토큰이 아닙니다.\"\n" +
                            "}")))

    @RequestMapping(method = RequestMethod.PUT, value = "/{questionId}")
    ResponseEntity<Void> updateQuestion(
            @PathVariable Long questionId,
            @RequestParam String content);

    @Operation(
            summary = "질문 삭제",
            description = "특정 질문을 삭제합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @Parameter(
            in = ParameterIn.HEADER,
            name = "Authorization", required = true,
            schema = @Schema(type = "string"),
            description = "Bearer [Access 토큰]")
    @ApiResponse(responseCode = "204", description = "질문 삭제 성공")
    @RequestMapping(method = RequestMethod.DELETE, value = "/{questionId}")
    ResponseEntity<Void> deleteQuestion(
            @PathVariable Long questionId);
}