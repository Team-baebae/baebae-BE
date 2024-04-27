package com.web.baebaeBE.infra.question.repository;

import com.web.baebaeBE.infra.question.entity.Question;
import com.web.baebaeBE.infra.question.entity.QuestionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;


public interface QuestionRepository{
    Optional<Question> findById(Long questionId);
    Question save(QuestionEntity questionEntity, Long memberId);
    Page<Question> findAllByMemberId(Long memberId, Pageable pageable);
}
