package com.example.application.data.service;

import com.example.application.data.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface INotificationRepository extends JpaRepository<Notification, Long>, JpaSpecificationExecutor<Notification> {
    List<Notification> findAllByReportId(Long id);

    List<Notification> findAllByUsername(String username);

    Notification findByUsernameAndReportId(String username, Long id);
}
