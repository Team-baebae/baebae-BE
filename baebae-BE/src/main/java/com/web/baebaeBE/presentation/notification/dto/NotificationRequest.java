package com.web.baebaeBE.presentation.notification.dto;

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
    private String questionContent;
  }

}
