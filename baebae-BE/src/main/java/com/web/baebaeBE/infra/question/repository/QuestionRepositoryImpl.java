package com.web.baebaeBE.infra.question.repository;

import com.web.baebaeBE.infra.question.entity.Question;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Primary
@RequiredArgsConstructor
public class QuestionRepositoryImpl implements QuestionRepository{
    private final QuestionJpaRepository questionJpaRepository;

    @Override
    public Optional<Question> findById(Long questionId) {
        return questionJpaRepository.findById(questionId);
    }

    @Override
    public Question save(Question questionEntity) {
        return questionJpaRepository.save(questionEntity);
    }

    @Override
    public Page<Question> findAllByMemberId(Long memberId, Pageable pageable) {
        return questionJpaRepository.findAllByMemberId(memberId, pageable);
    }

    @Override
    public void delete(Question questionEntity) {
        questionJpaRepository.delete(questionEntity);
    }
}


