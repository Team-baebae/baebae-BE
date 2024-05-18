package com.web.baebaeBE.domain.question.repository;

import com.web.baebaeBE.domain.question.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;


public interface QuestionRepository{
    Optional<Question> findById(Long questionId);
    Question save(Question questionEntity);
    Page<Question> findAllBySenderIdOrReceiverId(Long senderId, Long receiverId, Pageable pageable);
    Page<Question> findAllBySenderIdOrReceiverIdAndIsAnsweredTrue(Long senderId, Long receiverId, Pageable pageable);
    Page<Question> findAllBySenderIdOrReceiverIdAndIsAnsweredFalse(Long senderId, Long receiverId, Pageable pageable);
    void delete(Question questionEntity);
    long countBySenderIdOrReceiverIdAndIsAnsweredFalse(Long senderId, Long receiverId);
}
