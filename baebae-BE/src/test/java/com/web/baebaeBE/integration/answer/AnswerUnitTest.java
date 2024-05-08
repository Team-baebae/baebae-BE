package com.web.baebaeBE.integration.answer;

import com.web.baebaeBE.domain.answer.service.AnswerService;
import com.web.baebaeBE.global.image.s3.S3ImageStorageService;
import com.web.baebaeBE.infra.answer.entity.Answer;
import com.web.baebaeBE.infra.answer.repository.AnswerRepository;
import com.web.baebaeBE.infra.member.entity.Member;
import com.web.baebaeBE.infra.member.enums.MemberType;
import com.web.baebaeBE.infra.question.entity.Question;
import com.web.baebaeBE.presentation.answer.dto.AnswerCreateRequest;
import io.jsonwebtoken.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AnswerUnitTest {
    @Mock
    private AnswerService answerService;
    @Mock
    private Answer answer;
    @Mock
    private S3ImageStorageService s3ImageStorageService;
    @Mock
    private AnswerRepository answerRepository;
    private Member testMember;
    @Mock
    private Question question;
    private MultipartFile imageFile;
    private MultipartFile audioFile;
    private Answer existingAnswer;

    @BeforeEach
    void setup() throws Exception {
        // Member 객체 초기화
        testMember = Member.builder()
                .id(1L)
                .email("test@gmail.com")
                .nickname("장지효")
                .memberType(MemberType.KAKAO)
                .build();

        // Question 객체 초기화
        question = new Question(1L, testMember, "질문 내용", "장지효", true, LocalDateTime.now());

        // Answer 객체의 초기화
        answer = Answer.builder()
                .id(1L)
                .question(question)
                .member(testMember)
                .imageFiles(new ArrayList<>())
                .musicAudio("")
                .content("This is a test answer.")
                .build();

        // 파일
        imageFile = new MockMultipartFile("image", "image1.jpg", "image/jpeg", "image data".getBytes());
        audioFile = new MockMultipartFile("audio", "song.mp3", "audio/mpeg", "audio data".getBytes());


        // 모의 설정
        when(answerService.createAnswer(any(Answer.class), anyList(), any())).thenAnswer(invocation -> {
            Answer ans = invocation.getArgument(0);
            List<MultipartFile> images = invocation.getArgument(1);
            MultipartFile audio = invocation.getArgument(2);


            String imageFileUrl = s3ImageStorageService.uploadFile(
                    testMember.getId().toString(), ans.getId().toString(), "image", 0,
                    images.get(0).getInputStream(), images.get(0).getSize(), images.get(0).getContentType()
            );
            String audioFileUrl = s3ImageStorageService.uploadFile(
                    testMember.getId().toString(), ans.getId().toString(), "audio", 0,
                    audio.getInputStream(), audio.getSize(), audio.getContentType()
            );

            ans.getImageFiles().add(imageFileUrl);
            ans.setMusicAudio(audioFileUrl);
            return ans;
        });


        // S3 업로드 모의 설정
        when(s3ImageStorageService.uploadFile(anyString(), anyString(), anyString(), anyInt(), any(), anyLong(), anyString()))
                .thenReturn("https://example.com/image1.jpg", "https://example.com/song.mp3");
    }

    @Test
    void testFileUploads() throws IOException {
        Answer processedAnswer = answerService.createAnswer(answer, Arrays.asList(imageFile), audioFile);

        // S3 파일 업로드 검증
        verify(s3ImageStorageService).uploadFile(
                eq(testMember.getId().toString()), anyString(), eq("image"), eq(0), any(), eq(imageFile.getSize()), eq(imageFile.getContentType())
        );
        verify(s3ImageStorageService).uploadFile(
                eq(testMember.getId().toString()), anyString(), eq("audio"), eq(0), any(), eq(audioFile.getSize()), eq(audioFile.getContentType())
        );

        // 결과 URL 검증
        assertEquals("https://example.com/image1.jpg", processedAnswer.getImageFiles().get(0));
        assertEquals("https://example.com/song.mp3", processedAnswer.getMusicAudio());
    }

    @Test
    void testUpdateAnswer() throws IOException {
        // 입력 데이터 준비
        AnswerCreateRequest request = new AnswerCreateRequest();
        request.setContent("Updated content");
        request.setQuestionId(1L); // 질문 ID 설정
        request.setLinkAttachments(Arrays.asList("link1", "link2")); // 링크 첨부 파일 설정
        request.setMusicName("Updated Music Name"); // 음악 제목 설정
        request.setMusicSinger("Updated Music Singer"); // 음악 가수 설정
        request.setMusicAudioUrl("https://example.com/updated_audio.mp3"); // 음악 오디오 URL 설정

        // 이미지 파일 설정
        MultipartFile[] imageFiles = {
                new MockMultipartFile("image", "updated_image.jpg", "image/jpeg", "updated image data".getBytes())
        };

        // 오디오 파일 설정
        MultipartFile audioFile = new MockMultipartFile("audio", "updated_song.mp3", "audio/mpeg", "updated audio data".getBytes());

        // 목 객체 설정
        when(answerRepository.findByAnswerId(anyLong())).thenReturn(Optional.ofNullable(existingAnswer));
        when(s3ImageStorageService.uploadFile(anyString(), anyString(), anyString(), anyInt(), any(), anyLong(), anyString()))
                .thenReturn("https://example.com/updated_image.jpg")
                .thenReturn("https://example.com/updated_song.mp3");

        // 테스트 대상 메소드 호출
        Answer updatedAnswer = answerService.updateAnswer(1L, request, imageFiles, audioFile);

        // 상호 작용 검증
        verify(answerRepository).findByAnswerId(1L);
        verify(s3ImageStorageService, atLeastOnce()).uploadFile(anyString(), anyString(), anyString(), anyInt(), any(), anyLong(), anyString());
        assertEquals("Updated content", updatedAnswer.getContent());
        assertEquals(1L, updatedAnswer.getQuestion().getId());// 질문 ID가 올바르게 설정되었는지 확인
        assertEquals(Arrays.asList("link1", "link2"), updatedAnswer.getLinkAttachments()); // 링크 첨부 파일이 올바르게 설정되었는지 확인
        assertEquals("Updated Music Name", updatedAnswer.getMusicName()); // 음악 제목이 올바르게 설정되었는지 확인
        assertEquals("Updated Music Singer", updatedAnswer.getMusicSinger()); // 음악 가수가 올바르게 설정되었는지 확인
        assertEquals("https://example.com/updated_audio.mp3", updatedAnswer.getMusicAudio()); // 음악 오디오 URL이 올바르게 설정되었는지 확인
    }

    @Test
    void testDeleteAnswer() {
        Answer answer = Mockito.mock(Answer.class); // 가짜 객체 생성
        when(answerRepository.findByAnswerId(1L)).thenReturn(Optional.of(answer));
        doNothing().when(answerRepository).delete(answer);

        // Call
        answerService.deleteAnswer(1L);

        // Verify
        verify(answerRepository, times(1)).findByAnswerId(1L); // findByAnswerId 메소드가 한 번 호출되었는지 확인
        verify(answerRepository, times(1)).delete(answer); // delete 메소드가 한 번 호출되었는지 확인

    }
}

