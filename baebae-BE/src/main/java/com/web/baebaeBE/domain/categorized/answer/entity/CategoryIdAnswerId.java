package com.web.baebaeBE.domain.categorized.answer.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@EqualsAndHashCode
public class CategoryIdAnswerId implements Serializable {
    private Long category;
    private Long answer;
}