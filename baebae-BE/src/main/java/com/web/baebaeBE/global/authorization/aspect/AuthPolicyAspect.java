package com.web.baebaeBE.global.authorization.aspect;

import com.web.baebaeBE.domain.answer.entity.Answer;
import com.web.baebaeBE.domain.answer.exception.AnswerError;
import com.web.baebaeBE.domain.answer.repository.AnswerRepository;
import com.web.baebaeBE.domain.category.entity.Category;
import com.web.baebaeBE.domain.category.exception.CategoryException;
import com.web.baebaeBE.domain.category.repository.CategoryRepository;
import com.web.baebaeBE.domain.member.entity.Member;
import com.web.baebaeBE.domain.member.exception.MemberException;
import com.web.baebaeBE.domain.member.repository.MemberRepository;
import com.web.baebaeBE.domain.question.entity.Question;
import com.web.baebaeBE.domain.question.exception.QuestionError;
import com.web.baebaeBE.domain.question.repository.QuestionRepository;
import com.web.baebaeBE.global.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;


@Aspect
@Component
@RequiredArgsConstructor
public class AuthPolicyAspect {

    private final MemberRepository memberRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final CategoryRepository categoryRepository;

    @Before("@annotation(com.web.baebaeBE.global.authorization.annotation.AuthorizationMember) && args(memberId,..)")
    public void checkMember(JoinPoint joinPoint, Long memberId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentEmail = authentication.getName();
        System.out.println("checkMember: " + memberId);

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(MemberException.NOT_EXIST_MEMBER));

        if (!member.getEmail().equals(currentEmail)) {
            throw new BusinessException(MemberException.NOT_MATCH_MEMBER);
        }
    }

    @Before("@annotation(com.web.baebaeBE.global.authorization.annotation.AuthorizationQuestion) && args(questionId,..)")
    public void checkQuestion(JoinPoint joinPoint, Long questionId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentEmail = authentication.getName();
        System.out.println("checkQuestion: " + questionId);

        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new BusinessException(QuestionError.NO_EXIST_QUESTION));

        if (!question.getSender().getEmail().equals(currentEmail)) {
            throw new BusinessException(MemberException.NOT_MATCH_MEMBER);
        }
    }

    @Before("@annotation(com.web.baebaeBE.global.authorization.annotation.AuthorizationAnswer) && args(answerId,..)")
    public void checkAnswer(JoinPoint joinPoint, Long answerId) {
        Object[] args = joinPoint.getArgs();
        System.out.println(args[0]);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentEmail = authentication.getName();
        System.out.println("checkAnswer: " + answerId);

        Answer answer = answerRepository.findByAnswerId(answerId)
                .orElseThrow(() -> new BusinessException(AnswerError.NO_EXIST_ANSWER));

        if (!answer.getMember().getEmail().equals(currentEmail)) {
            throw new BusinessException(MemberException.NOT_MATCH_MEMBER);
        }
    }

    @Before("@annotation(com.web.baebaeBE.global.authorization.annotation.AuthorizationCategory) && args(categoryId,..)")
    public void checkCategory(JoinPoint joinPoint, Long categoryId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentEmail = authentication.getName();
        System.out.println("checkCategory: " + categoryId);

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new BusinessException(CategoryException.CATEGORY_NOT_FOUND));

        if (!category.getMember().getEmail().equals(currentEmail)) {
            throw new BusinessException(MemberException.NOT_MATCH_MEMBER);
        }
    }

    @Before("@annotation(com.web.baebaeBE.global.authorization.annotation.AuthorizationCategoryAndAnswer) && args(categoryId,answerId,..)")
    public void checkCategoryAndAnswer(JoinPoint joinPoint, Long categoryId, Long answerId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentEmail = authentication.getName();
        System.out.println("checkCategoryAndAnswer: " + categoryId + " " + answerId);

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new BusinessException(CategoryException.CATEGORY_NOT_FOUND));

        Answer answer = answerRepository.findByAnswerId(answerId)
                .orElseThrow(() -> new BusinessException(AnswerError.NO_EXIST_ANSWER));

        if (!category.getMember().getEmail().equals(currentEmail) && !answer.getMember().getEmail().equals(currentEmail)) {
            throw new BusinessException(MemberException.NOT_MATCH_MEMBER);
        }
    }
}