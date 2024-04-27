package com.web.baebaeBE.infra.question.repository;

import com.web.baebaeBE.infra.question.entity.Question;
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
    private final QuestionMapper questionMapper;

    public QuestionRepositoryImpl(QuestionJpaRepository questionJpaRepository, QuestionMapper questionMapper) {
        this.questionJpaRepository = questionJpaRepository;
        this.questionMapper = questionMapper;
    }

    @Override
    public Optional<Question> findById(Long questionId) {
        return questionJpaRepository.findById(questionId)
                .map(questionMapper::toDomain);
    }

    @Override
    public Question save(Question question, Long memberId) {
        QuestionEntity questionEntity = questionMapper.toEntity(question, memberId);
        questionEntity = questionJpaRepository.save(questionEntity);
        return questionMapper.toDomain(questionEntity);
    }

    @Override
    public Page<Question> findAllByMemberId(Long memberId, Pageable pageable) {
        Page<QuestionEntity> questionEntitiesPage = questionJpaRepository.findAllByMemberId(memberId, pageable);
        return questionEntitiesPage.map(questionMapper::toDomain);
    }
}


