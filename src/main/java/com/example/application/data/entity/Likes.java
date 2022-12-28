package com.example.application.data.entity;

import javax.persistence.Entity;

@Entity
public class Likes extends AbstractEntity {

    private String username;
    private Long reportId;
    private Long commentId;


    public Likes(String username, Long reportId, Long commentId) {
        this.username = username;
        this.reportId = reportId;
        this.commentId = commentId;
    }

    public Likes() {

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

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }
}
