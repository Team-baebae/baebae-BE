package com.web.baebaeBE.infra.question.repository;

import com.web.baebaeBE.infra.question.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;


public interface QuestionRepository{
    Optional<Question> findById(Long questionId);
    Question save(Question questionEntity);
    Page<Question> findAllByMemberId(Long memberId, Pageable pageable);
    Page<Question> findAllAnsweredQuestionsByMemberId(Long memberId, Pageable pageable);
    Page<Question> findAllUnansweredQuestionsByMemberId(Long memberId, Pageable pageable);
    void delete(Question questionEntity);

}
