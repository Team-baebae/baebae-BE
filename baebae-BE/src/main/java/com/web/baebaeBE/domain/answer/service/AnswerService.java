package com.web.baebaeBE.domain.answer.service;

import com.web.baebaeBE.domain.answer.dto.AnswerDetailResponse;
import com.web.baebaeBE.domain.answer.dto.AnswerResponse;
import com.web.baebaeBE.domain.answer.exception.AnswerError;
import com.web.baebaeBE.domain.answer.repository.AnswerMapper;
import com.web.baebaeBE.domain.categorized.answer.entity.CategorizedAnswer;
import com.web.baebaeBE.domain.categorized.answer.repository.CategorizedAnswerRepository;
import com.web.baebaeBE.domain.category.entity.Category;
import com.web.baebaeBE.domain.login.exception.LoginException;
import com.web.baebaeBE.domain.member.entity.Member;
import com.web.baebaeBE.domain.member.repository.MemberRepository;
import com.web.baebaeBE.domain.notification.dto.NotificationRequest;
import com.web.baebaeBE.domain.notification.service.NotificationService;
import com.web.baebaeBE.domain.question.repository.QuestionRepository;
import com.web.baebaeBE.global.error.exception.BusinessException;
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AnswerService {
    private final AnswerRepository answerRepository;
    private final MemberRepository memberRepository;
    private final QuestionRepository questionRepository;
    private final CategorizedAnswerRepository categorizedAnswerRepository;
    private final NotificationService notificationService;
    private final S3ImageStorageService s3ImageStorageService;
    private final AnswerMapper answerMapper;

    @Transactional
    public AnswerDetailResponse createAnswer(AnswerCreateRequest request, Long memberId, MultipartFile imageFile) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(LoginException.NOT_EXIST_MEMBER));
        Question question = questionRepository.findById(request.getQuestionId())
                .orElseThrow(() -> new BusinessException(AnswerError.NO_EXIST_QUESTION));

        Answer answer = answerMapper.toEntity(request, question, member);
        Answer savedAnswer = answerRepository.save(answer);
        if (!imageFile.isEmpty()) {
            try (InputStream inputStream = imageFile.getInputStream()) {
                String imageUrl = s3ImageStorageService.uploadFile(member.getId().toString(), answer.getId().toString(), "image", 0, inputStream, imageFile.getSize(), imageFile.getContentType());
                answer.setImageFiles(List.of(imageUrl));
            } catch (IOException e) {
                throw new BusinessException(AnswerError.IMAGE_PROCESSING_ERROR);
            }
        }
        // 알림 생성 및 전송
        NotificationRequest.create notificationDto = new NotificationRequest.create(
                member.getId(),
                "귀하의 질문에 새로운 답변이 등록되었습니다!",
                question.getContent(),
                NotificationRequest.EventType.NEW_ANSWER,
                null
        );
        notificationService.createNotification(notificationDto);

        // 질문의 isAnswered 상태를 true로 업데이트
        question.setAnswered(true);
        questionRepository.save(question);

        return answerMapper.toDomain(savedAnswer, "FCM token needed");
    }

    @Transactional
    public List<AnswerResponse> getAnswersByMemberId(Long memberId) {
        List<Answer> answers = answerRepository.findByMemberId(memberId);
        return answers.stream()
                .map(answerMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public Page<AnswerDetailResponse> getAllAnswers(Long memberId, Pageable pageable) {
        Page<Answer> answerPage = answerRepository.findAllByMemberId(memberId, pageable);
        return answerPage.map(answer -> answerMapper.toDomain(answer, "FCM token needed"));
    }

    @Transactional
    public AnswerDetailResponse updateAnswer(Long answerId, AnswerCreateRequest request, MultipartFile imageFile) {
        Answer answer = answerRepository.findByAnswerId(answerId)
                .orElseThrow(() -> new BusinessException(AnswerError.NO_EXIST_ANSWER));
        answer.setContent(request.getContent());
        answer.setLinkAttachments(request.getLinkAttachments());
        answer.setMusicName(request.getMusicName());
        answer.setMusicSinger(request.getMusicSinger());
        if (!imageFile.isEmpty()) {
            try (InputStream inputStream = imageFile.getInputStream()) {
                String imageUrl = s3ImageStorageService.uploadFile(answer.getMember().getId().toString(), answerId.toString(), "image", 0, inputStream, imageFile.getSize(), imageFile.getContentType());
                answer.setImageFiles(List.of(imageUrl));
            } catch (IOException e) {
                throw new BusinessException(AnswerError.IMAGE_PROCESSING_ERROR);
            }
        }
        // 질문의 isAnswered 상태를 true로 업데이트
        Question question = answer.getQuestion();
        question.setAnswered(true);

        questionRepository.save(question);
        answer = answerRepository.save(answer);
        return answerMapper.toDomain(answer, "FCM token needed");
    }

    @Transactional
    public void deleteAnswer(Long answerId) {
        Answer answer = answerRepository.findByAnswerId(answerId)
                .orElseThrow(() -> new IllegalArgumentException("No answer found with id " + answerId));
        answerRepository.delete(answer);
    }

    @Transactional
    public void updateReactionCounts(Long answerId, int heartCount, int curiousCount, int sadCount) {
        Answer answer = answerRepository.findByAnswerId(answerId)
                .orElseThrow(() -> new BusinessException(AnswerError.NO_EXIST_ANSWER));
        answer.setHeartCount(heartCount);
        answer.setCuriousCount(curiousCount);
        answer.setSadCount(sadCount);
        answerRepository.save(answer);

        // 알림 생성 및 전송
        if (answer.getMember().getFcmToken() != null) {
            NotificationRequest.create notificationDto = new NotificationRequest.create(
                    answer.getMember().getId(),
                    "답변에 새로운 반응이 있습니다!",
                    String.format("하트: %d, 궁금해요: %d, 슬퍼요: %d", heartCount, curiousCount, sadCount),
                    NotificationRequest.EventType.REACTION,
                    "updated"
            );
            notificationService.createNotification(notificationDto);
        }
    }

}