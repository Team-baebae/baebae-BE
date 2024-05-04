package com.web.baebaeBE.presentation.answer;

import com.web.baebaeBE.application.answer.AnswerApplication;
import com.web.baebaeBE.presentation.answer.dto.AnswerCreateRequest;
import com.web.baebaeBE.presentation.answer.dto.AnswerDetailResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@AllArgsConstructor
@RequestMapping("/api/answers")
public class AnswerController {
    private final AnswerApplication answerApplication;

    @Operation(summary = "피드 생성")
    @PostMapping(value = "/member/{memberId}", consumes = "multipart/form-data")
    public ResponseEntity<AnswerDetailResponse> createAnswer(@PathVariable Long memberId, @ModelAttribute AnswerCreateRequest request) {
        AnswerDetailResponse createdAnswer = answerApplication.createAnswer(request, memberId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAnswer);
    }

    @Operation(summary = "피드 조회")
    @GetMapping
    public ResponseEntity<Page<AnswerDetailResponse>> getAllAnswers(@RequestParam Long memberId, Pageable pageable) {
        Page<AnswerDetailResponse> answers = answerApplication.getAllAnswers(memberId, pageable);
        return ResponseEntity.ok(answers);
    }

    @Operation(summary = "피드 수정")
    @PutMapping(value = "/{answerId}", consumes = "multipart/form-data")
    public ResponseEntity<AnswerDetailResponse> updateAnswer(@PathVariable Long answerId, @ModelAttribute AnswerCreateRequest request,
                                                             @RequestParam("imageFiles") MultipartFile[] imageFiles) {
        AnswerDetailResponse updatedAnswer = answerApplication.updateAnswer(answerId, request, imageFiles);
        return ResponseEntity.ok(updatedAnswer);
    }

    @Operation(summary = "피드 삭제")
    @DeleteMapping("/{answerId}")
    public ResponseEntity<Void> deleteAnswer(@PathVariable Long answerId) {
        answerApplication.deleteAnswer(answerId);
        return ResponseEntity.noContent().build();
    }
}
