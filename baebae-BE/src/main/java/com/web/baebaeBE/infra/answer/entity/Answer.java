
package com.web.baebaeBE.infra.answer.entity;

import com.web.baebaeBE.global.converter.StringListConverter;
import com.web.baebaeBE.infra.member.entity.Member;
import com.web.baebaeBE.infra.question.entity.Question;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "answer")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answer_id", updatable = false, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "link_attachment", nullable = true, length = 255)
    private String linkAttachment;

    @Column(name = "music_search", nullable = true, length = 255)
    private String musicSearch;

    @Convert(converter = StringListConverter.class)
    @Column(name = "image_file", nullable = true)
    private List<String> imageFiles;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "like_count", nullable = false)
    private Integer likeCount;

    public static Answer of(Long id, Question question, Member member, String content,
                            String linkAttachment, String musicSearch, List<String> imageFiles,
                            LocalDateTime createdDate, Integer likeCount) {
        return new Answer(id, question, member, content,linkAttachment, musicSearch, imageFiles,
                createdDate, likeCount);
    }

}
