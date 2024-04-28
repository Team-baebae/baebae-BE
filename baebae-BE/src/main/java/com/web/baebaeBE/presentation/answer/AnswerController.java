package com.web.baebaeBE.presentation.answer;

import com.web.baebaeBE.domain.answer.service.AnswerService;
import com.web.baebaeBE.infra.answer.entity.Answer;
import com.web.baebaeBE.presentation.answer.dto.AnswerCreateRequest;
import com.web.baebaeBE.presentation.answer.dto.AnswerDetailResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/answers")
public class AnswerController {
    private final AnswerService answerService;

    @Autowired
    public AnswerController(AnswerService answerService) {
        this.answerService = answerService;
    }

    @Operation(summary = "피드 생성")
    @PostMapping("/member/{memberId}")
    public ResponseEntity<Answer> createAnswer(@RequestBody AnswerCreateRequest request) {
        Answer createdAnswer = answerService.createAnswer(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAnswer);
    }

    @Operation(summary = "피드 조회")
    @GetMapping
    public ResponseEntity<List<AnswerDetailResponse>> getAllAnswers(@RequestParam Long memberId, Pageable pageable) {
        List<AnswerDetailResponse> answers = answerService.getAllAnswers(memberId, pageable).getContent();
        return new ResponseEntity<>(answers, HttpStatus.OK);
    }

    @Operation(summary = "피드 수정")
    @PutMapping("/{answerId}")
    public ResponseEntity <AnswerDetailResponse> updateAnswer(@PathVariable Long answerId, @RequestBody AnswerCreateRequest request) {
        AnswerDetailResponse updatedAnswer = answerService.updateAnswer(answerId, request);
        return ResponseEntity.ok(updatedAnswer);
    }

    @Operation(summary = "피드 삭제")
    @DeleteMapping("/{answerId}")
    public ResponseEntity<Void> deleteAnswer(@PathVariable Long answerId) {
        answerService.deleteAnswer(answerId);
        return ResponseEntity.noContent().build();
    }

}
