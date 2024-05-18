package com.web.baebaeBE.domain.music.repository;

import com.web.baebaeBE.domain.music.entity.Music;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MusicRepositoryImpl implements MusicRepository{
    private final MusicJpaRepository jpaRepository;

    @Autowired
    public MusicRepositoryImpl(MusicJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Music save(Music music) {
        return jpaRepository.save(music);
    }

    @Override
    public void delete(Music music) {
        jpaRepository.delete(music);
    }

    @Override
    public Music findById(Long id) {
        return jpaRepository.findById(id).orElse(null);
    }
}