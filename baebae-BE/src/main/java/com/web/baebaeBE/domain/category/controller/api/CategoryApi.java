package com.web.baebaeBE.domain.category.controller.api;

import com.web.baebaeBE.domain.category.dto.CategoryRequest;
import com.web.baebaeBE.domain.category.dto.CategoryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@SecurityRequirement(name = "baererAuth")
@Tag(name = "Category", description = "카테고리 관련 API")
public interface CategoryApi {

    @Operation(summary = "카테고리 생성",
            description = "멤버 ID, 카테고리 이미지 파일, 카테고리 정보를 받아 새로운 카테고리를 생성합니다.",
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
                            schema = @Schema(implementation = CategoryResponse.CategoryInformationResponse.class)))
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<CategoryResponse.CategoryInformationResponse> createCategory(
            @Parameter(description = "멤버의 ID", required = true) @PathVariable Long memberId,
            @Parameter(description = "카테고리 이미지 파일", required = true, schema = @Schema(type = "string", format = "binary")) @RequestPart(value = "categoryImage", required = false) MultipartFile categoryImage,
            @Parameter(description = "생성할 카테고리 정보", required = true) @RequestPart("createCategory") CategoryRequest.CreateCategory createCategory
    );


    @Operation(summary = "멤버의 모든 카테고리 조회",
            description = "멤버 ID를 받아 해당 멤버의 모든 카테고리를 조회합니다."
    )
    @Parameter(
            in = ParameterIn.HEADER,
            name = "Authorization", required = false,
            schema = @Schema(type = "string"),
            description = "Bearer [Access 토큰]")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CategoryResponse.CategoryListResponse.class)))
    })
    @GetMapping("/{memberId}")
    ResponseEntity<CategoryResponse.CategoryListResponse> getCategoriesByMember(
            @Parameter(description = "멤버의 ID", required = true) @PathVariable Long memberId
    );

    @Operation(summary = "카테고리에 단일 답변 추가",
            description = "카테고리 ID와 답변 ID를 받아 해당 답변을 카테고리에 추가합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @Parameter(
            in = ParameterIn.HEADER,
            name = "Authorization", required = true,
            schema = @Schema(type = "string"),
            description = "Bearer [Access 토큰]")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "추가 성공")
    })
    @PostMapping("/{categoryId}/answers/{answerId}")
    ResponseEntity<Void> addAnswerToCategory(
            @Parameter(description = "카테고리의 ID", required = true) @PathVariable Long categoryId,
            @Parameter(description = "답변의 ID", required = true) @PathVariable Long answerId
    );

    @Operation(summary = "카테고리 이미지 수정",
            description = "카테고리 ID와 이미지 파일을 받아 해당 카테고리의 이미지를 수정합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @Parameter(
            in = ParameterIn.HEADER,
            name = "Authorization", required = true,
            schema = @Schema(type = "string"),
            description = "Bearer [Access 토큰]")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "수정 성공")
    })
    @PatchMapping("/{categoryId}/image")
    ResponseEntity<CategoryResponse.CategoryInformationResponse> updateCategoryImage(
            @Parameter(description = "카테고리의 ID", required = true) @PathVariable Long categoryId,
            @Parameter(description = "이미지 파일", required = true) @RequestPart("imageFile") MultipartFile imageFile
    );

    @Operation(summary = "카테고리 수정",
            description = "카테고리 ID와 새로운 카테고리 이름및 답변(피드) 리스트를 받아 카테고리를 수정합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @Parameter(
            in = ParameterIn.HEADER,
            name = "Authorization", required = true,
            schema = @Schema(type = "string"),
            description = "Bearer [Access 토큰]")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수정 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CategoryResponse.CategoryInformationResponse.class)))
    })
    @PatchMapping("/{categoryId}/image")
    ResponseEntity<CategoryResponse.CategoryInformationResponse> updateCategory(
            @Parameter(description = "카테고리의 ID", required = true) @PathVariable Long categoryId,
            @Parameter(description = "수정할 카테고리 정보", required = true) @RequestBody CategoryRequest.UpdateCategory updateCategory
    );

    @Operation(summary = "카테고리 삭제",
            description = "카테고리 ID를 받아 해당 카테고리를 삭제합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @Parameter(
            in = ParameterIn.HEADER,
            name = "Authorization", required = true,
            schema = @Schema(type = "string"),
            description = "Bearer [Access 토큰]")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "삭제 성공")
    })
    @DeleteMapping("/{categoryId}")
    ResponseEntity<Void> deleteCategory(
            @Parameter(description = "카테고리의 ID", required = true) @PathVariable Long categoryId
    );
}