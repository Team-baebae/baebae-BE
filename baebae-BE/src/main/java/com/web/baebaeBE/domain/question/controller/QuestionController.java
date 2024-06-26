package com.web.baebaeBE.domain.question.controller;

import com.web.baebaeBE.domain.question.controller.api.QuestionApi;
import com.web.baebaeBE.domain.question.dto.QuestionCreateRequest;
import com.web.baebaeBE.domain.question.dto.QuestionDetailResponse;
import com.web.baebaeBE.domain.question.service.QuestionService;
import com.web.baebaeBE.global.authorization.annotation.AuthorizationMember;
import com.web.baebaeBE.global.authorization.annotation.AuthorizationQuestion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
public class QuestionController implements QuestionApi {
    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @PostMapping("/sender/{senderId}/receiver/{receiverId}")
    @AuthorizationMember
    public ResponseEntity<QuestionDetailResponse> createQuestion(
            @PathVariable Long senderId,
            @PathVariable Long receiverId,
            @RequestBody QuestionCreateRequest request) {
        QuestionDetailResponse createdQuestion = questionService.createQuestion(request, senderId, receiverId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdQuestion);
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<QuestionDetailResponse>> getAllQuestions(
            @PathVariable Long memberId, Pageable pageable) {
        Page<QuestionDetailResponse> questions = questionService.getQuestionsByMemberId(memberId, pageable);
        return ResponseEntity.ok(questions.getContent());
    }

    @GetMapping("/answered/{memberId}")
    public ResponseEntity<List<QuestionDetailResponse>> getAnsweredQuestions(
            @PathVariable Long memberId, Pageable pageable) {
        Page<QuestionDetailResponse> questions = questionService.getAnsweredQuestions(memberId, pageable);
        return ResponseEntity.ok(questions.getContent());
    }

    @GetMapping("/unanswered/{memberId}")
    public ResponseEntity<List<QuestionDetailResponse>> getUnansweredQuestions(
            @PathVariable Long memberId, Pageable pageable) {
        Page<QuestionDetailResponse> questions = questionService.getUnansweredQuestions(memberId, pageable);
        return ResponseEntity.ok(questions.getContent());
    }

    @DeleteMapping("/{questionId}")
    @AuthorizationQuestion
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long questionId) {
        questionService.deleteQuestion(questionId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @GetMapping("/unanswered/count/{memberId}")
    public ResponseEntity<Long> getUnansweredQuestionCount(@PathVariable Long memberId) {
        long count = questionService.getUnansweredQuestionCount(memberId);
        return ResponseEntity.ok(count);
    }
}