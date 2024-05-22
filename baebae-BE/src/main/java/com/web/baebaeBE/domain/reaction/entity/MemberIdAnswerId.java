package com.web.baebaeBE.domain.reaction.entity;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@EqualsAndHashCode
public class MemberIdAnswerId implements Serializable {
    private Long member;
    private Long answer;
}
