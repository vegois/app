package com.example.application.data.service.imp;

import com.example.application.data.entity.Notification;
import com.example.application.data.service.INotificationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    private final INotificationRepository notificationRepository;

    public NotificationService(INotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public List<Notification> findAllByReportId(Long id){
        return notificationRepository.findAllByReportId(id);
    }

    public List<Notification> findAllByUsername(String username){
        return notificationRepository.findAllByUsername(username);
    }

    public Notification findByUsernameAndReportId(String username, Long id) {
        return notificationRepository.findByUsernameAndReportId(username,id);
    }

    public void delete(Notification notification) {
        notificationRepository.delete(notification);
    }

    public void save(Notification notification) {
        notificationRepository.save(notification);
    }
}
