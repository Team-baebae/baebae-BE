package com.web.baebaeBE.domain.notification.dto;

import com.web.baebaeBE.domain.notification.entity.Notification;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


public class NotificationResponse {

  @Getter
  @Setter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class NotificationContentResponse {

    @Schema(example = "1")
    private Long notificationId;
    @Schema(example = "배승우님이 질문을 남기셨습니다! 확인해보세요")
    private String notificationContent;
    @Schema(example = "가은아! 넌 무슨색상을 좋아해?")
    private String questionContent;
    @Schema(type = "string", example = "2024-05-02 07:10:48")
    private LocalDateTime notificationTime;

    public static NotificationResponse.NotificationContentResponse of(Notification notification) {
      return NotificationContentResponse.builder()
              .notificationId(notification.getId())
              .notificationContent(notification.getNotificationContent())
              .questionContent(notification.getDetailContent())
              .notificationTime(notification.getNotificationTime())
              .build();
    }

    public static NotificationListResponse ListOf(List<Notification> notificationList) {
      List<NotificationResponse.NotificationContentResponse> responseList = notificationList.stream()
              .map(notification -> NotificationResponse.NotificationContentResponse.builder()
                      .notificationId(notification.getId())
                      .notificationContent(notification.getNotificationContent())
                      .questionContent(notification.getDetailContent())
                      .notificationTime(notification.getNotificationTime())
                      .build())
              .collect(Collectors.toList());

      return NotificationListResponse.builder()
              .notificationList(responseList)
              .build();
    }

  }
  @Getter
  @Setter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class NotificationListResponse{
    private List<NotificationContentResponse> notificationList;


  }
}
