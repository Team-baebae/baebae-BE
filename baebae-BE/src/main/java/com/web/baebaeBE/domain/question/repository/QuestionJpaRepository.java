package com.web.baebaeBE.domain.question.repository;

import com.web.baebaeBE.domain.question.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionJpaRepository extends JpaRepository<Question, Long> {
    Page<Question> findAllByMemberId(Long memberId, Pageable pageable);
    Page<Question> findAllByMemberIdAndIsAnsweredTrue(Long memberId, Pageable pageable);
    Page<Question> findAllByMemberIdAndIsAnsweredFalse(Long memberId, Pageable pageable);

}
