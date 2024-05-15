package com.web.baebaeBE.domain.reaction.service;

import com.web.baebaeBE.domain.answer.entity.Answer;
import com.web.baebaeBE.domain.reaction.entity.ReactionValue;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ReactionUpdateService {

    public void increaseReactionCount(Answer answer, ReactionValue reaction) {
        switch (reaction) {
            case HEART: // 좋아요
                answer.setHeartCount(answer.getHeartCount() + 1);
                break;
            case CURIOUS: // 궁금해요
                answer.setCuriousCount(answer.getCuriousCount() + 1);
                break;
            case SAD: // 슬퍼요
                answer.setSadCount(answer.getSadCount() + 1);
                break;
            case CONNECTION: // 통했당
                answer.setConnectCount(answer.getConnectCount() + 1);
                break;
        }
    }

    public void decreaseReactionCount(Answer answer, ReactionValue reaction) {
        switch (reaction) {
            case HEART: // 좋아요
                answer.setHeartCount(answer.getHeartCount() - 1);
                break;
            case CURIOUS: // 궁금해요
                answer.setCuriousCount(answer.getCuriousCount() - 1);
                break;
            case SAD: // 슬퍼요
                answer.setSadCount(answer.getSadCount() - 1);
                break;
            case CONNECTION: // 통했당
                answer.setConnectCount(answer.getConnectCount() - 1);
                break;
        }
    }
}
