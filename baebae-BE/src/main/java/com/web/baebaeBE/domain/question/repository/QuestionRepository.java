package com.web.baebaeBE.domain.question.repository;

import com.web.baebaeBE.domain.question.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;


public interface QuestionRepository{
    Optional<Question> findById(Long questionId);
    Question save(Question questionEntity);
    Page<Question> findAllByReceiverId(Long receiverId, Pageable pageable);
    Page<Question> findAllByReceiverIdAndIsAnsweredTrue(Long receiverId, Pageable pageable);
    Page<Question> findAllByReceiverIdAndIsAnsweredFalse(Long receiverId, Pageable pageable);
    void delete(Question questionEntity);
    long countByReceiverIdAndIsAnsweredFalse(Long receiverId);
}
