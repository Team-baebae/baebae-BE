package com.web.baebaeBE.domain.answer.controller;

import com.web.baebaeBE.domain.answer.controller.api.AnswerApi;
import com.web.baebaeBE.domain.answer.dto.AnswerCreateRequest;
import com.web.baebaeBE.domain.answer.dto.AnswerDetailResponse;
import com.web.baebaeBE.domain.answer.dto.AnswerResponse;
import com.web.baebaeBE.domain.answer.service.AnswerService;
import com.web.baebaeBE.domain.category.entity.Category;
import com.web.baebaeBE.domain.category.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/answers")
public class AnswerController implements AnswerApi {
    private final AnswerService answerService;
    private final CategoryService categoryService;
    @PostMapping(value = "/{memberId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AnswerDetailResponse> createAnswer(@PathVariable Long memberId,
                                                             @RequestPart(value = "imageFile") MultipartFile imageFile,
                                                             @RequestPart AnswerCreateRequest request) {
        AnswerDetailResponse createdAnswer = answerService.createAnswer(request, memberId, imageFile);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAnswer);
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<AnswerResponse>> getAnswersByMemberId(@PathVariable Long memberId) {
        List<AnswerResponse> answers = answerService.getAnswersByMemberId(memberId);
        return ResponseEntity.ok(answers);
    }

    @GetMapping()
    public ResponseEntity<Page<AnswerDetailResponse>> getAllAnswers(
            @RequestParam Long memberId,
            @RequestParam(required = false) Long category,
            Pageable pageable) {

        Category cat = null;
        if (category != null) {
            cat = categoryService.getCategoryByNameOrId(category);
        }
        Page<AnswerDetailResponse> answers = answerService.getAllAnswers(memberId, cat, pageable);
        return ResponseEntity.ok(answers);
    }

    @PutMapping(value = "/{answerId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AnswerDetailResponse> updateAnswer(@PathVariable Long answerId,
                                                             @RequestPart(value = "imageFile") MultipartFile imageFile,
                                                             @RequestPart AnswerCreateRequest request) {
        AnswerDetailResponse updatedAnswer = answerService.updateAnswer(answerId, request, imageFile);
        return ResponseEntity.ok(updatedAnswer);
    }

    @DeleteMapping("/{answerId}")
    public ResponseEntity<Void> deleteAnswer(@PathVariable Long answerId) {
        answerService.deleteAnswer(answerId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @Operation(summary = "반응 알림")
    @PatchMapping("/{answerId}/react")
    public ResponseEntity<Void> updateAnswerReactions(@PathVariable Long answerId,
                                                      @RequestParam int heartCount,
                                                      @RequestParam int curiousCount,
                                                      @RequestParam int sadCount) {
        answerService.updateReactionCounts(answerId, heartCount, curiousCount, sadCount);
        return ResponseEntity.ok().build();
    }
}
