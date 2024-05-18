package com.web.baebaeBE.domain.music.entity;

import com.web.baebaeBE.domain.answer.entity.Answer;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "answer_id", nullable = false)
    private Answer answer;

    @Column(name = "music_name", nullable = false)
    private String musicName;

    @Column(name = "music_audo_url", nullable = false)
    private String musicAudioUrl;

    @Column(name = "music_singer", nullable = false)
    private String musicSinger;

    public static Music of(Long id, Answer answer, String  musicName, String  musicAudioUrl, String musicSinger) {
        return new Music(id, answer, musicName, musicAudioUrl, musicSinger);
    }
}