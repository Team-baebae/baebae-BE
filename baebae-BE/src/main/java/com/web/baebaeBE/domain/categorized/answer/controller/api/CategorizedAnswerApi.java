package com.web.baebaeBE.domain.categorized.answer.controller.api;

import com.web.baebaeBE.domain.categorized.answer.dto.CategorizedAnswerRequest;
import com.web.baebaeBE.domain.categorized.answer.dto.CategorizedAnswerResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(name = "CategorizedAnswer", description = "카테고리 내부의 피드에 관련된 API")
public interface CategorizedAnswerApi {

    @Operation(summary = "피드가 속한 카테고리 조회",
            description = "Answer ID를 받아 해당 Answer에 연결된 모든 카테고리를 조회합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @Parameter(
            in = ParameterIn.HEADER,
            name = "Authorization", required = true,
            schema = @Schema(type = "string"),
            description = "Bearer [Access 토큰]")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    @GetMapping("{answerId}")
    ResponseEntity<List<CategorizedAnswerResponse.CategoryInformationResponse>> getCategoriesByAnswerId(
            @Parameter(description = "Answer의 ID", required = true) @PathVariable Long answerId
    );


    @Operation(summary = "피드가 속한 카테고리 수정",
            description = "Answer ID와 Category ID 리스트를 받아 피드가 속할 카테고리 정보를 수정합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @Parameter(
            in = ParameterIn.HEADER,
            name = "Authorization", required = true,
            schema = @Schema(type = "string"),
            description = "Bearer [Access 토큰]")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수정 성공")
    })
    @PutMapping("/{answerId}")
    public ResponseEntity<Void> updateCategoriesByAnswerId(@PathVariable Long answerId, @RequestBody CategorizedAnswerRequest.CategoryList categoryIds) ;
}