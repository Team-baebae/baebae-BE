package com.web.baebaeBE.domain.question.repository;

import com.web.baebaeBE.domain.question.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;


public interface QuestionRepository{
    Optional<Question> findById(Long questionId);
    Question save(Question questionEntity);
    Page<Question> findAllByMemberId(Long memberId, Pageable pageable);
    Page<Question> findAllByMemberIdAndIsAnsweredTrue(Long memberId, Pageable pageable);
    Page<Question> findAllByMemberIdAndIsAnsweredFalse(Long memberId, Pageable pageable);
    void delete(Question questionEntity);

}
