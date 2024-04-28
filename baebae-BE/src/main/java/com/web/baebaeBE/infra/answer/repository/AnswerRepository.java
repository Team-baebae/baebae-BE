package com.web.baebaeBE.infra.answer.repository;

import com.web.baebaeBE.infra.answer.entity.Answer;
import com.web.baebaeBE.infra.question.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface AnswerRepository {
    Optional<Answer> findByAnswerId(Long answerId);
    Answer save(Answer answer);
    Page<Answer> findAllByMemberId(Long memberId, Pageable pageable);
    void delete(Answer answer);
}
