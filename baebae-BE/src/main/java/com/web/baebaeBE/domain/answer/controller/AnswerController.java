package com.web.baebaeBE.domain.answer.controller;

import com.web.baebaeBE.domain.answer.controller.api.AnswerApi;
import com.web.baebaeBE.domain.answer.dto.AnswerCreateRequest;
import com.web.baebaeBE.domain.answer.dto.AnswerDetailResponse;
import com.web.baebaeBE.domain.answer.dto.AnswerResponse;
import com.web.baebaeBE.domain.answer.service.AnswerService;

import com.web.baebaeBE.domain.reaction.entity.ReactionValue;
import com.web.baebaeBE.global.authorization.annotation.AuthorizationAnswer;
import com.web.baebaeBE.global.authorization.annotation.AuthorizationMember;
import com.web.baebaeBE.global.authorization.annotation.AuthorizationQuestion;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/answers")
public class AnswerController implements AnswerApi {
    private final AnswerService answerService;

    @PostMapping(value = "/{memberId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @AuthorizationMember
    public ResponseEntity<AnswerDetailResponse> createAnswer(@PathVariable Long memberId,
                                                             @RequestPart(value = "imageFile") MultipartFile imageFile,
                                                             @RequestPart AnswerCreateRequest request) {
        AnswerDetailResponse createdAnswer = answerService.createAnswer(request, memberId, imageFile);
        return ResponseEntity.ok(createdAnswer);
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<Page<AnswerDetailResponse>> getAllAnswers(
            @PathVariable Long memberId,
            @RequestParam(required = false) Long categoryId,
            Pageable pageable) {

        return ResponseEntity.ok(answerService.getAllAnswers(memberId, categoryId, pageable));
    }

    @PutMapping(value = "/{answerId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @AuthorizationAnswer
    public ResponseEntity<AnswerDetailResponse> updateAnswer(@PathVariable Long answerId,
                                                             @RequestPart(value = "imageFile", required = false) MultipartFile imageFile,
                                                             @RequestPart AnswerCreateRequest request) {
        AnswerDetailResponse updatedAnswer = answerService.updateAnswer(answerId, request, imageFile);
        return ResponseEntity.ok(updatedAnswer);
    }

    @DeleteMapping("/{answerId}")
    @AuthorizationAnswer
    public ResponseEntity<Void> deleteAnswer(@PathVariable Long answerId) {
        answerService.deleteAnswer(answerId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @GetMapping("/{answerId}/reacted")
    public ResponseEntity<Map<ReactionValue, Boolean>> hasReacted(@PathVariable Long answerId,
                                                                  @RequestParam Long memberId) {
        Map<ReactionValue, Boolean> hasReacted = answerService.hasReacted(answerId, memberId);
        return ResponseEntity.ok(hasReacted);
    }

}
