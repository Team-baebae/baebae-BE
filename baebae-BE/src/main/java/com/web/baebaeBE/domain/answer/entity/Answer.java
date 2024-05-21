package com.web.baebaeBE.domain.answer.entity;

import com.web.baebaeBE.domain.categorized.answer.entity.CategorizedAnswer;
import com.web.baebaeBE.domain.category.entity.Category;
import com.web.baebaeBE.domain.member.entity.Member;
import com.web.baebaeBE.domain.music.entity.Music;
import com.web.baebaeBE.domain.question.entity.Question;
import com.web.baebaeBE.domain.reaction.entity.ReactionValue;
import com.web.baebaeBE.domain.reactioncount.entity.ReactionCount;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
    @Column(name = "answer_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;


    @Column(name = "image_file")
    private String imageFile;  // 이미지 파일 경로를 저장하는 리스트

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;

    @OneToOne(mappedBy = "answer", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Music music;

    @Column(name = "link_attachment")
    private String linkAttachments;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "answer", cascade = CascadeType.REMOVE)
    private List<CategorizedAnswer> categorizedAnswers;

    @OneToOne(mappedBy = "answer", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private ReactionCount reactionCount;

    @Column(name = "profile_on_off", nullable = false)
    private boolean profileOnOff;

    public static Answer of(Long id, Question question, Member member, String content,
                            String imageFile, Music music, String linkAttachments, String imageUrl, LocalDateTime createdDate,
                            ReactionCount reactionCount, boolean profileOnOff) {
        return new Answer(id, question, member, imageFile, content, music, linkAttachments, imageUrl, createdDate, null, reactionCount, profileOnOff);
    }

}
