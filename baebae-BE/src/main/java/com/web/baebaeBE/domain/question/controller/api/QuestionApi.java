package com.web.baebaeBE.domain.question.controller.api;

import com.web.baebaeBE.domain.question.dto.QuestionCreateRequest;
import com.web.baebaeBE.domain.question.dto.QuestionDetailResponse;
import com.web.baebaeBE.domain.question.dto.QuestionUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@SecurityRequirement(name = "baererAuth")
@Tag(name = "Question", description = "질문 관리 API")
public interface QuestionApi {

    @Operation(
            summary = "질문 생성",
            description = "특정 회원에 대한 새로운 질문을 생성합니다.",
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
    @PostMapping("/sender/{senderId}/receiver/{receiverId}")
    ResponseEntity<QuestionDetailResponse> createQuestion(
            @RequestBody QuestionCreateRequest request,
            @PathVariable Long senderId, @PathVariable Long receiverId);

    @Operation(
            summary = "모든 질문 조회",
            description = "특정 회원의 모든 질문을 페이지네이션으로 조회합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @Parameter(
            in = ParameterIn.HEADER,
            name = "Authorization", required = true,
            schema = @Schema(type = "string"),
            description = "Bearer [Access 토큰]")
    @ApiResponse(responseCode = "200", description = "질문 조회 성공",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = List.class)))
    @GetMapping("/member/{memberId}")
    ResponseEntity<List<QuestionDetailResponse>> getAllQuestions(
            @PathVariable Long memberId, Pageable pageable);

    @Operation(
            summary = "답변된 질문 조회",
            description = "특정 회원의 답변된 모든 질문을 페이지네이션으로 조회합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @Parameter(
            in = ParameterIn.HEADER,
            name = "Authorization", required = true,
            schema = @Schema(type = "string"),
            description = "Bearer [Access 토큰]")
    @ApiResponse(responseCode = "200", description = "답변된 질문 조회 성공")
    @GetMapping("/answered/{memberId}")
    ResponseEntity<List<QuestionDetailResponse>> getAnsweredQuestions(
            @PathVariable Long memberId, Pageable pageable);

    @Operation(
            summary = "답변되지 않은 질문 조회",
            description = "특정 회원의 답변되지 않은 모든 질문을 페이지네이션으로 조회합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @Parameter(
            in = ParameterIn.HEADER,
            name = "Authorization", required = true,
            schema = @Schema(type = "string"),
            description = "Bearer [Access 토큰]")
    @ApiResponse(responseCode = "200", description = "답변되지 않은 질문 조회 성공")
    @GetMapping("/unanswered/{memberId}")
    ResponseEntity<List<QuestionDetailResponse>> getUnansweredQuestions(
            @PathVariable Long memberId, Pageable pageable);

    @Operation(
            summary = "질문 수정",
            description = "기존 질문의 내용을 수정합니다.",
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
    @PutMapping("/{questionId}")
    ResponseEntity<Void> updateQuestion(
            @PathVariable Long questionId, @RequestBody QuestionUpdateRequest request);

    @Operation(
            summary = "질문 삭제",
            description = "기존 질문을 삭제합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @Parameter(
            in = ParameterIn.HEADER,
            name = "Authorization", required = true,
            schema = @Schema(type = "string"),
            description = "Bearer [Access 토큰]")
    @ApiResponse(responseCode = "204", description = "질문 삭제 성공")
    @DeleteMapping("/{questionId}")
    ResponseEntity<Void> deleteQuestion(@PathVariable Long questionId);

    @Operation(
            summary = "답변되지 않은 질문 개수 조회",
            description = "특정 회원의 답변되지 않은 질문의 개수를 조회합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @Parameter(
            in = ParameterIn.HEADER,
            name = "Authorization", required = true,
            schema = @Schema(type = "string"),
            description = "Bearer [Access 토큰]")
    @ApiResponse(responseCode = "200", description = "답변되지 않은 질문 개수 조회 성공",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Long.class)))
    @GetMapping("/unanswered/count/{memberId}")
    ResponseEntity<Long> getUnansweredQuestionCount(@PathVariable Long memberId);
}
