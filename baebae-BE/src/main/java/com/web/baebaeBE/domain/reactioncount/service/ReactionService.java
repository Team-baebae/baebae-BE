package com.web.baebaeBE.domain.reactioncount.service;

import com.web.baebaeBE.domain.answer.entity.Answer;
import com.web.baebaeBE.domain.answer.exception.AnswerError;
import com.web.baebaeBE.domain.answer.repository.AnswerRepository;
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
    private final AnswerRepository answerRepository;

    @Transactional
    public ReactionResponse.CountReactionInformationDto getReactionCounts(Long answerId) {
        ReactionCount reactionCount = reactionCountJpaRepository.findByAnswerId(answerId);
        if (reactionCount == null) {
            Answer answer = answerRepository.findByAnswerId(answerId)
                    .orElseThrow(() -> new BusinessException(AnswerError.NO_EXIST_ANSWER));
            reactionCount = ReactionCount.of(answer, 0, 0, 0, 0);
            reactionCountJpaRepository.save(reactionCount);
        }
        return ReactionResponse.CountReactionInformationDto.of(reactionCount);
    }
}
