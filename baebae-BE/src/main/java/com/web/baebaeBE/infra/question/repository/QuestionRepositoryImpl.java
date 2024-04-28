package com.web.baebaeBE.infra.question.repository;

import com.web.baebaeBE.infra.question.entity.QuestionEntity;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Primary
public class QuestionRepositoryImpl implements QuestionRepository{
    private final QuestionJpaRepository questionJpaRepository;
//    private final QuestionMapper questionMapper;

    public QuestionRepositoryImpl(QuestionJpaRepository questionJpaRepository /*, QuestionMapper questionMapper*/) {
        this.questionJpaRepository = questionJpaRepository;
//        this.questionMapper = questionMapper;
    }

    @Override
    public Optional<QuestionEntity> findById(Long questionId) {
        return questionJpaRepository.findById(questionId);
    }

    @Override
    public QuestionEntity save(QuestionEntity questionEntity) {
        return questionJpaRepository.save(questionEntity);
    }

    @Override
    public Page<QuestionEntity> findAllByMemberId(Long memberId, Pageable pageable) {
        return questionJpaRepository.findAllByMemberId(memberId, pageable);
    }

    @Override
    public void delete(QuestionEntity questionEntity) {
        questionJpaRepository.delete(questionEntity);
    }
}



