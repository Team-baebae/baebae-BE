package com.web.baebaeBE.domain.music.repository;

import com.web.baebaeBE.domain.music.entity.Music;

import java.util.List;

public interface MusicRepository {
    Music save(Music music);
    List<Music> findByAnswerId(Long answerId);
    void delete(Music music);
    Music findById(Long id);
}