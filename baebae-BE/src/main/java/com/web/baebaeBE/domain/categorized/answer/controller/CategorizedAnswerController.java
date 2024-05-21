package com.web.baebaeBE.domain.categorized.answer.controller;

import com.web.baebaeBE.domain.categorized.answer.controller.api.CategorizedAnswerApi;
import com.web.baebaeBE.domain.categorized.answer.dto.CategorizedAnswerResponse;
import com.web.baebaeBE.domain.categorized.answer.service.CategorizedAnswerService;
import com.web.baebaeBE.global.authorization.annotation.AuthorizationAnswer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categorizedAnswer")
public class CategorizedAnswerController implements CategorizedAnswerApi {

    private final CategorizedAnswerService categorizedAnswerService;


    @GetMapping("{answerId}")
    @AuthorizationAnswer
    public ResponseEntity<List<CategorizedAnswerResponse.CategoryInformationResponse>> getCategoriesByAnswerId(
            @PathVariable Long answerId
    ) {
        return ResponseEntity.ok(categorizedAnswerService.getCategoriesByAnswerId(answerId));
    }


}
