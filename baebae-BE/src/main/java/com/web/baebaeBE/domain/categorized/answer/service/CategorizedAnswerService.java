package com.web.baebaeBE.domain.categorized.answer.service;

import com.web.baebaeBE.domain.answer.dto.AnswerDetailResponse;
import com.web.baebaeBE.domain.answer.repository.AnswerMapper;
import com.web.baebaeBE.domain.categorized.answer.entity.CategorizedAnswer;
import com.web.baebaeBE.domain.categorized.answer.repository.CategorizedAnswerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CategorizedAnswerService {
    private final CategorizedAnswerRepository categorizedAnswerRepository;
    private final AnswerMapper answerMapper;

    public Page<AnswerDetailResponse> getAnswersByMemberAndCategory(Long memberId, Long categoryId, Pageable pageable) {
        Page<CategorizedAnswer> categorizedAnswers;
        if (categoryId == null) { // 카테고리 정보가 없을 경우 -> 전체 피드 조회
            categorizedAnswers = categorizedAnswerRepository.findByAnswer_Member_Id(memberId, pageable);
        } else  // 카테고리 정보가 있을 경우 -> 해당 카테고리 피드 조회
            categorizedAnswers = categorizedAnswerRepository.findByAnswer_Member_IdAndCategory_Id(memberId, categoryId, pageable);

        return categorizedAnswers.map(categorizedAnswer -> answerMapper.toDomain(categorizedAnswer.getAnswer()));
    }
}
