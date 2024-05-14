package com.web.baebaeBE.domain.reaction.dto;

import com.web.baebaeBE.domain.reaction.entity.ReactionValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


public class ReactionRequest {

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class create{
    private ReactionValue reaction;
  }

}
