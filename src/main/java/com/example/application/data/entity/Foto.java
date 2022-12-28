package com.example.application.data.entity;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;

@Entity
public class Foto extends AbstractEntity{
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] pic;

    private Long playerId;
    private Long reportId;
    private Long commentId;

    public Foto(byte[] pic, Long userId, Long reportId, Long commentId) {
        this.pic = pic;
        this.playerId = userId;
        this.reportId = reportId;
        this.commentId = commentId;
    }

    public Foto() {

    }

    public byte[] getPic() {
        return pic;
    }

    public void setPic(byte[] pic) {
        this.pic = pic;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
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
