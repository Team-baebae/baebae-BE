package com.web.baebaeBE.infra.question.repository;

import com.web.baebaeBE.infra.question.entity.QuestionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;


public interface QuestionRepository{
//    Optional<Question> findById(Long questionId);
    Optional<QuestionEntity> findById(Long questionId);
    QuestionEntity save(QuestionEntity questionEntity);
    Page<QuestionEntity> findAllByMemberId(Long memberId, Pageable pageable);
    void delete(QuestionEntity questionEntity);

}

