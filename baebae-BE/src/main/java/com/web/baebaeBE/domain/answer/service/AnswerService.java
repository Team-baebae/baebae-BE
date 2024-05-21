package com.web.baebaeBE.domain.answer.service;

import com.web.baebaeBE.domain.answer.dto.AnswerDetailResponse;
import com.web.baebaeBE.domain.answer.dto.AnswerResponse;
import com.web.baebaeBE.domain.answer.exception.AnswerError;
import com.web.baebaeBE.domain.answer.repository.AnswerMapper;
import com.web.baebaeBE.domain.categorized.answer.entity.CategorizedAnswer;
import com.web.baebaeBE.domain.categorized.answer.repository.CategorizedAnswerRepository;
import com.web.baebaeBE.domain.login.exception.LoginException;
import com.web.baebaeBE.domain.member.entity.Member;
import com.web.baebaeBE.domain.member.repository.MemberRepository;
import com.web.baebaeBE.domain.notification.dto.NotificationRequest;
import com.web.baebaeBE.domain.notification.service.NotificationService;
import com.web.baebaeBE.domain.question.repository.QuestionRepository;
import com.web.baebaeBE.domain.reaction.entity.ReactionValue;
import com.web.baebaeBE.domain.reaction.repository.MemberAnswerReactionRepository;
import com.web.baebaeBE.domain.reactioncount.entity.ReactionCount;
import com.web.baebaeBE.domain.reactioncount.repository.ReactionCountJpaRepository;
import com.web.baebaeBE.global.error.exception.BusinessException;
import com.web.baebaeBE.global.firebase.FirebaseNotificationService;
import com.web.baebaeBE.global.image.s3.S3ImageStorageService;
import com.web.baebaeBE.domain.answer.entity.Answer;
import com.web.baebaeBE.domain.answer.repository.AnswerRepository;
import com.web.baebaeBE.domain.question.entity.Question;
import com.web.baebaeBE.domain.answer.dto.AnswerCreateRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AnswerService {


    private final AnswerRepository answerRepository;
    private final MemberRepository memberRepository;
    private final QuestionRepository questionRepository;
    private final CategorizedAnswerRepository categorizedAnswerRepository;
    private final MemberAnswerReactionRepository memberAnswerReactionRepository;
    private final NotificationService notificationService;
    private final S3ImageStorageService s3ImageStorageService;
    private final AnswerMapper answerMapper;
    private final ReactionCountJpaRepository reactionCountJpaRepository;
    private final FirebaseNotificationService firebaseNotificationService;

    @Transactional
    public AnswerDetailResponse createAnswer(AnswerCreateRequest request, Long memberId, MultipartFile imageFile) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(LoginException.NOT_EXIST_MEMBER));
        Question question = questionRepository.findById(request.getQuestionId())
                .orElseThrow(() -> new BusinessException(AnswerError.NO_EXIST_QUESTION));

        if(question.isAnswered() == true)
            throw new BusinessException(AnswerError.ALREADY_ANSWERED_QUESTION); // 이미 답변한 질문이면 예외처리

        // Answer 생성
        Answer answer = answerMapper.toEntity(request, question, member);
        Answer savedAnswer = answerRepository.save(answer);
        String imageUrl;
        if (!imageFile.isEmpty()) {
            try (InputStream inputStream = imageFile.getInputStream()) {
                imageUrl = s3ImageStorageService.uploadFile(member.getId().toString(), answer.getId().toString(), "image", 0, inputStream, imageFile.getSize(), imageFile.getContentType());
                answer.setImageFile(imageUrl);
            } catch (IOException e) {
                throw new BusinessException(AnswerError.IMAGE_PROCESSING_ERROR);
            }
        } else{
            imageUrl = s3ImageStorageService.getDefaultFileUrl(); // 기본이미지
        }
        answer.setImageFile(imageUrl);

        // ReactionCount 생성
        ReactionCount reactionCount = ReactionCount.builder()
                .answer(savedAnswer)
                .heartCount(0)
                .curiousCount(0)
                .sadCount(0)
                .connectCount(0)
                .build();
        reactionCountJpaRepository.save(reactionCount);

        // 질문의 isAnswered 상태를 true로 업데이트
        question.setAnswered(true);
        questionRepository.save(question);

        firebaseNotificationService.notifyNewAnswer(question.getSender(), question,savedAnswer); // 푸시 메세지 전송

        return answerMapper.toDomain(savedAnswer);
    }


    @Transactional
    public Page<AnswerDetailResponse> getAllAnswers(Long memberId, Long categoryId, Pageable pageable) {
        if (categoryId == null) {
            Page<Answer> answerPage = answerRepository.findAllByMemberId(memberId, pageable);
            return answerPage.map(answerMapper::toDomain);
        } else {
            Page<CategorizedAnswer> categorizedAnswerPage = categorizedAnswerRepository.findByAnswer_Member_IdAndCategory_Id(memberId, categoryId, pageable);
            Page<Answer> answerPage = categorizedAnswerPage.map(CategorizedAnswer::getAnswer);
            return answerPage.map(answerMapper::toDomain);
        }
    }

    @Transactional
    public AnswerDetailResponse updateAnswer(Long answerId, AnswerCreateRequest request, MultipartFile imageFile) {
        Answer answer = answerRepository.findByAnswerId(answerId)
                .orElseThrow(() -> new BusinessException(AnswerError.NO_EXIST_ANSWER));
        answer.setContent(request.getContent());
        answer.setLinkAttachments(request.getLinkAttachments());

        // Music 엔티티 업데이트
        answer.getMusic().setMusicName(request.getMusicName());
        answer.getMusic().setMusicSinger(request.getMusicSinger());
        answer.getMusic().setMusicAudioUrl(request.getMusicAudioUrl());

        if (request.isUpdateImage() && imageFile != null && !imageFile.isEmpty()) {
            try (InputStream inputStream = imageFile.getInputStream()) {
                String imageUrl = s3ImageStorageService.uploadFile(answer.getMember().getId().toString(), answerId.toString(), "image", 0, inputStream, imageFile.getSize(), imageFile.getContentType());
                answer.setImageFile(imageUrl);
            } catch (IOException e) {
                throw new BusinessException(AnswerError.IMAGE_PROCESSING_ERROR);
            }
        }

        // 질문의 isAnswered 상태를 true로 업데이트
        Question question = answer.getQuestion();
        question.setAnswered(true);

        questionRepository.save(question);
        answer = answerRepository.save(answer);
        return answerMapper.toDomain(answer);
    }

    @Transactional
    public void deleteAnswer(Long answerId) {
        Answer answer = answerRepository.findByAnswerId(answerId)
                .orElseThrow(() -> new BusinessException(AnswerError.NO_EXIST_ANSWER));
        answerRepository.delete(answer);
    }

    @Transactional
    public Map<ReactionValue, Boolean> hasReacted(Long answerId, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(LoginException.NOT_EXIST_MEMBER));
        Answer answer = answerRepository.findByAnswerId(answerId)
                .orElseThrow(() -> new BusinessException(AnswerError.NO_EXIST_ANSWER));

        Map<ReactionValue, Boolean> reactionStatus = new HashMap<>();
        for (ReactionValue reactionValue : ReactionValue.values()) {
            boolean hasReacted = memberAnswerReactionRepository.findByMemberAndAnswerAndReaction(member, answer, reactionValue).isPresent();
            reactionStatus.put(reactionValue, hasReacted);
        }
        return reactionStatus;
    }
}
