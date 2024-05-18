package com.web.baebaeBE.domain.question.repository;

import com.web.baebaeBE.domain.question.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionJpaRepository extends JpaRepository<Question, Long> {
    Page<Question> findAllByReceiverId(Long receiverId, Pageable pageable);
    Page<Question> findAllByReceiverIdAndIsAnsweredTrue(Long receiverId, Pageable pageable);
    Page<Question> findAllByReceiverIdAndIsAnsweredFalse(Long receiverId, Pageable pageable);
    long countByReceiverIdAndIsAnsweredFalse(Long receiverId);
}