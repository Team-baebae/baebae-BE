package com.web.baebaeBE.domain.music.repository;

import com.web.baebaeBE.domain.music.entity.Music;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MusicJpaRepository extends JpaRepository<Music,Long> {
    List<Music> findByAnswerId(Long answerId);
}