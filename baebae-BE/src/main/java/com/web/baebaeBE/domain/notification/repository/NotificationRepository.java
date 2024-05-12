package com.web.baebaeBE.domain.notification.repository;

import com.web.baebaeBE.domain.notification.entity.Notification;
import com.web.baebaeBE.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByMemberOrderByNotificationTimeDesc(Member member);
}