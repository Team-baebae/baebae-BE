package com.web.baebaeBE.domain.question.repository;

import com.web.baebaeBE.domain.question.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionJpaRepository extends JpaRepository<Question, Long> {
    Page<Question> findAllBySenderIdOrReceiverId(Long senderId, Long receiverId, Pageable pageable);
    Page<Question> findAllBySenderIdOrReceiverIdAndIsAnsweredTrue(Long senderId, Long receiverId, Pageable pageable);
    Page<Question> findAllBySenderIdOrReceiverIdAndIsAnsweredFalse(Long senderId, Long receiverId, Pageable pageable);
    long countBySenderIdOrReceiverIdAndIsAnsweredFalse(Long senderId, Long receiverId);
}