package com.web.baebaeBE.domain.reaction.controller;

import com.web.baebaeBE.domain.reaction.controller.api.MemberAnswerReactionApi;
import com.web.baebaeBE.domain.reaction.dto.ReactionRequest;
import com.web.baebaeBE.domain.reaction.dto.ReactionResponse;
import com.web.baebaeBE.domain.reaction.entity.MemberAnswerReaction;
import com.web.baebaeBE.domain.reaction.service.MemberAnswerReactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reactions")
@RequiredArgsConstructor
public class MemberAnswerReactionController implements MemberAnswerReactionApi {
    private final MemberAnswerReactionService memberAnswerReactionService;

    @PostMapping("/{memberId}/{answerId}")
    public ResponseEntity<ReactionResponse.ReactionInformationDto> createReaction(
            @PathVariable Long memberId,
            @PathVariable Long answerId,
            @RequestBody ReactionRequest.create reactionDto) {

        return ResponseEntity.ok(memberAnswerReactionService.createReaction(memberId, answerId, reactionDto.getReaction()));
    }

    // 통했당~
    @PostMapping("/connection/{memberId}/{answerId}/{destinationMemberId}")
    public ResponseEntity<ReactionResponse.ConnectionReactionInformationDto> createClickReaction(
            @PathVariable Long memberId, // 자기자신 (통했당 하는 주체)
            @PathVariable Long answerId, // 피드정보
            @RequestParam(required = false) Long destinationMemberId // 피드작성자가 통했당 누군지 대상 지정
    ) {
        // destinationMemberId 파라미터 여부로 체크
        if(destinationMemberId == null)
            return ResponseEntity.ok(memberAnswerReactionService.createConnectionReaction(memberId, answerId));
        else
            return ResponseEntity.ok(memberAnswerReactionService.connectConnectionReaction(memberId, answerId, destinationMemberId));
    }
}