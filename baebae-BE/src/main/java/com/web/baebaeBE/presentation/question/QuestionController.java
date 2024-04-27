package com.web.baebaeBE.presentation.question;

import com.web.baebaeBE.domain.question.service.QuestionService;
import com.web.baebaeBE.presentation.question.dto.QuestionCreateRequest;
import com.web.baebaeBE.presentation.question.dto.QuestionDetailResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {
    private final QuestionService questionService;

    @Autowired
    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @Operation(summary = "질문 생성")
    @PostMapping("/member/{memberId}")
    public ResponseEntity<QuestionDetailResponse> createQuestion(@RequestBody QuestionCreateRequest questionDTO, @PathVariable Long memberId) {
        QuestionDetailResponse createdQuestion = questionService.createQuestion(memberId, questionDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdQuestion);
    }

    @Operation(summary = "질문 조회")
    @GetMapping()
    public ResponseEntity<List
    <QuestionDetailResponse>> getAllQuestions(@RequestParam Long memberId, Pageable pageable) {
        List<QuestionDetailResponse> questions = questionService.getAllQuestions(memberId, pageable).getContent();
        return new ResponseEntity<>(questions, HttpStatus.OK);
    }

    @Operation(summary = "질문 수정")
    @PutMapping("/{questionId}")
    public ResponseEntity<Void> updateQuestion(@PathVariable Long questionId, @RequestParam String content) {
        questionService.updateQuestion(questionId, content);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @Operation(summary = "질문 삭제")
    @DeleteMapping("/{questionId}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long questionId) {
        questionService.deleteQuestion(questionId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
