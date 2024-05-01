package com.web.baebaeBE.presentation.question;

import com.web.baebaeBE.application.question.QuestionApplication;
import com.web.baebaeBE.presentation.question.dto.QuestionCreateRequest;
import com.web.baebaeBE.presentation.question.dto.QuestionDetailResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {
    private final QuestionApplication questionApplication;

    @Autowired
    public QuestionController(QuestionApplication questionApplication) {
        this.questionApplication = questionApplication;
    }

    @Operation(summary = "질문 생성")
    @PostMapping("/member/{memberId}")
    public ResponseEntity<QuestionDetailResponse> createQuestion(@RequestBody QuestionCreateRequest questionDTO, @PathVariable Long memberId) {
        QuestionDetailResponse createdQuestion = questionApplication.createQuestion(questionDTO, memberId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdQuestion);
    }

    @Operation(summary = "질문 조회")
    @GetMapping()
    public ResponseEntity<List<QuestionDetailResponse>> getAllQuestions(@RequestParam Long memberId, Pageable pageable) {
        Page<QuestionDetailResponse> questions = questionApplication.getAllQuestions(memberId, pageable);
        return ResponseEntity.ok(questions.getContent());
    }

    @Operation(summary = "질문 수정")
    @PutMapping("/{questionId}")
    public ResponseEntity<Void> updateQuestion(@PathVariable Long questionId, @RequestParam String content) {
        questionApplication.updateQuestion(questionId, content);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @Operation(summary = "질문 삭제")
    @DeleteMapping("/{questionId}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long questionId) {
        questionApplication.deleteQuestion(questionId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
