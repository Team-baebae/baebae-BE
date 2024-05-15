package com.web.baebaeBE.domain.music.entity;

import com.web.baebaeBE.domain.answer.entity.Answer;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "music")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Music {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id", updatable = false, nullable = false)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "answer_id", nullable = false)
    private Answer answer;

    @Column(name = "music_name", nullable = false)
    private String musicName;

    @Column(name = "music_picture", nullable = false)
    private String musicPicture;

    @Column(name = "music_singer", nullable = false)
    private String musicSinger;

    public static Music of(Long id, Answer answer, String  musicName, String  musicPicture, String musicSinger) {
        return new Music(id, answer, musicName, musicPicture, musicSinger);
    }
}