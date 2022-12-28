package com.example.application.data.entity;

import javax.persistence.Entity;

@Entity
public class Notification extends AbstractEntity{

    private String username;
    private Long reportId;

    public Notification(String username, Long reportId) {
        this.username = username;
        this.reportId = reportId;
    }

    public Notification() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getReportId() {
        return reportId;
    }

    public void setReportId(Long reportId) {
        this.reportId = reportId;
    }
}
