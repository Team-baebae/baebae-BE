package com.web.baebaeBE.presentation.question;

import com.web.baebaeBE.application.question.QuestionApplication;
import com.web.baebaeBE.presentation.question.api.QuestionApi;
import com.web.baebaeBE.presentation.question.dto.QuestionCreateRequest;
import com.web.baebaeBE.presentation.question.dto.QuestionDetailResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
public class QuestionController implements QuestionApi {
    private final QuestionApplication questionApplication;


    @Autowired
    public QuestionController(QuestionApplication questionApplication) {
        this.questionApplication = questionApplication;
    }

    @PostMapping("/member/{memberId}")
    public ResponseEntity<QuestionDetailResponse> createQuestion(
            @RequestBody QuestionCreateRequest questionDTO, @PathVariable Long memberId) {

        // 질문 생성 요청
        QuestionDetailResponse createdQuestion = questionApplication.createQuestion(questionDTO, memberId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdQuestion);
    }


//    @GetMapping("/member/{memberId}")
//    public ResponseEntity<List<QuestionDetailResponse>> getAllQuestions(
//            @RequestParam Long memberId, Pageable pageable) {
//        Page<QuestionDetailResponse> questions = questionApplication.getAllQuestions(memberId, pageable);
//        return ResponseEntity.ok(questions.getContent());
//    }

    @GetMapping("/answered/{memberId}")
    public ResponseEntity<List<QuestionDetailResponse>> getAnsweredQuestions(
            @PathVariable Long memberId, Pageable pageable) {
        Page<QuestionDetailResponse> questions = questionApplication.getAnsweredQuestions(memberId, pageable);
        return ResponseEntity.ok(questions.getContent());
    }

    @GetMapping("/unanswered/{memberId}")
    public ResponseEntity<List<QuestionDetailResponse>> getUnansweredQuestions(
            @PathVariable Long memberId, Pageable pageable) {
        Page<QuestionDetailResponse> questions = questionApplication.getUnansweredQuestions(memberId, pageable);
        return ResponseEntity.ok(questions.getContent());
    }


    @PutMapping("/{questionId}")
    public ResponseEntity<Void> updateQuestion(@PathVariable Long questionId, @RequestParam String content) {

        questionApplication.updateQuestion(questionId, content);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }


    @DeleteMapping("/{questionId}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long questionId) {
        questionApplication.deleteQuestion(questionId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
