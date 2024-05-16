package com.web.baebaeBE.domain.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


public class NotificationRequest {

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class create{
    private Long memberId;
    private String notificationContent;
    private String detailContent;
    private EventType eventType;
    private String reactionType;
  }

  public enum EventType {
    NEW_QUESTION, // 새로운 질문
    NEW_ANSWER,   // 새로운 답변
    REACTION      // 반응
  }
}
