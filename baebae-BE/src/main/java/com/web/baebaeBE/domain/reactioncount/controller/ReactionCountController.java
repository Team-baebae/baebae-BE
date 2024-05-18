package com.web.baebaeBE.domain.reactioncount.controller;

import com.web.baebaeBE.domain.reactioncount.controller.api.ReactionCountApi;
import com.web.baebaeBE.domain.reactioncount.dto.ReactionResponse;
import com.web.baebaeBE.domain.reactioncount.service.ReactionService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/reactionsCount")
public class ReactionCountController implements ReactionCountApi {

    private final ReactionService reactionService;



    @GetMapping("/{answerId}/reactionsCount")
    public ResponseEntity<ReactionResponse.CountReactionInformationDto> getReactionCounts(@PathVariable Long answerId) {
        ReactionResponse.CountReactionInformationDto reactionCounts = reactionService.getReactionCounts(answerId);
        return ResponseEntity.ok(reactionCounts);
    }
}
