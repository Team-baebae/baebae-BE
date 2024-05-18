package com.web.baebaeBE.domain.reactioncount.service;

import com.web.baebaeBE.domain.answer.exception.AnswerError;
import com.web.baebaeBE.domain.reactioncount.dto.ReactionResponse;
import com.web.baebaeBE.domain.reactioncount.entity.ReactionCount;
import com.web.baebaeBE.domain.reactioncount.repository.ReactionCountJpaRepository;
import com.web.baebaeBE.global.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ReactionService {
    private final ReactionCountJpaRepository reactionCountJpaRepository;

    @Transactional
    public ReactionResponse.CountReactionInformationDto getReactionCounts(Long answerId) {
        ReactionCount reactionCount = reactionCountJpaRepository.findByAnswerId(answerId)
                .orElseThrow(() -> new BusinessException(AnswerError.NO_EXIST_ANSWER));

        return ReactionResponse.CountReactionInformationDto.of(reactionCount);
    }
}
