package com.web.baebaeBE.domain.reaction.service;

import com.web.baebaeBE.domain.answer.entity.Answer;
import com.web.baebaeBE.domain.reaction.entity.ReactionValue;
import com.web.baebaeBE.domain.reactioncount.entity.ReactionCount;
import com.web.baebaeBE.domain.reactioncount.repository.ReactionCountJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ReactionUpdateService {

    private final ReactionCountJpaRepository reactionCountJpaRepository;

    public void increaseReactionCount(ReactionCount reactionCount, ReactionValue reaction) {
        switch (reaction) {
            case HEART: // 좋아요
                reactionCount.setHeartCount(reactionCount.getHeartCount() + 1);
                break;
            case CURIOUS: // 궁금해요
                reactionCount.setCuriousCount(reactionCount.getCuriousCount() + 1);
                break;
            case SAD: // 슬퍼요
                reactionCount.setSadCount(reactionCount.getSadCount() + 1);
                break;
            case CONNECT: // 통했당
                reactionCount.setConnectCount(reactionCount.getConnectCount() + 1);
                break;
        }
        reactionCountJpaRepository.save(reactionCount);
    }

    public void decreaseReactionCount(ReactionCount reactionCount, ReactionValue reaction) {
        switch (reaction) {
            case HEART: // 좋아요
                reactionCount.setHeartCount(reactionCount.getHeartCount() - 1);
                break;
            case CURIOUS: // 궁금해요
                reactionCount.setCuriousCount(reactionCount.getCuriousCount() - 1);
                break;
            case SAD: // 슬퍼요
                reactionCount.setSadCount(reactionCount.getSadCount() - 1);
                break;
            case CONNECT: // 통했당
                reactionCount.setConnectCount(reactionCount.getConnectCount() - 1);
                break;
        }
        reactionCountJpaRepository.save(reactionCount);
    }
}
