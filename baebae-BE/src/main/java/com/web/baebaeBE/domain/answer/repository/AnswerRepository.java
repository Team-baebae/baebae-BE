package com.web.baebaeBE.domain.answer.repository;

import com.web.baebaeBE.domain.answer.entity.Answer;
import com.web.baebaeBE.domain.category.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface AnswerRepository {
    Optional<Answer> findByAnswerId(Long answerId);
    List<Answer> findByMemberId(Long memberId);
    Answer save(Answer answer);
    Page<Answer> findAllByMemberId(Long memberId, Pageable pageable);
    void delete(Answer answer);
}
