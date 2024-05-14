package com.web.baebaeBE.domain.answer.entity;

import com.web.baebaeBE.domain.categorized.answer.entity.CategorizedAnswer;
import com.web.baebaeBE.domain.category.entity.Category;
import com.web.baebaeBE.domain.member.entity.Member;
import com.web.baebaeBE.domain.music.entity.Music;
import com.web.baebaeBE.domain.question.entity.Question;
import com.web.baebaeBE.domain.reaction.entity.ReactionValue;
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
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @ElementCollection
    @CollectionTable(name = "answer_image_files", joinColumns = @JoinColumn(name = "answer_id"))
    @Column(name = "image_file")
    private List<String> imageFiles;  // 이미지 파일 경로를 저장하는 리스트

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;


    @OneToOne(mappedBy = "answer", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Music music;

    @ElementCollection
    @CollectionTable(name = "answer_link_attachments", joinColumns = @JoinColumn(name = "answer_id"))
    @Column(name = "link_attachment")
    private List<String> linkAttachments;

    @Column(name = "heart_count", nullable = false)
    private int heartCount;

    @Column(name = "curious_count", nullable = false)
    private int curiousCount;

    @Column(name = "sad_count", nullable = false)
    private int sadCount;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "answer", cascade = CascadeType.REMOVE)
    private List<CategorizedAnswer> categorizedAnswers;



    public static Answer of(Long id, Question question, Category category, Member member, String content,
                            List<String> imageFiles, Music music, List<String> linkAttachments, int heartCount,
                            int curiousCount, int sadCount, LocalDateTime createdDate) {

        return new Answer(id, question, category, member, imageFiles, content, music, linkAttachments, heartCount,
                curiousCount, sadCount, createdDate,null);
    }

    public void increaseReactionCount(ReactionValue reaction) {
        switch (reaction) {
            case HEART: // 좋아요
                this.heartCount++;
                break;
            case CURIOUS: // 궁금해요
                this.curiousCount++;
                break;
            case SAD: // 슬퍼요
                this.sadCount++;
                break;
        }
    }
}

