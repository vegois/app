package com.example.application.data.entity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.Date;
import java.util.Optional;

@Entity
public class Question extends AbstractEntity {


    @ManyToOne
    private User author;
    private String text;
    private String topic;

    private String city;
    private Boolean hidden;
    private String image;
    private String category;
    private Date updatedDate;
    private Integer likes;
    private Integer commentCount;
    private Integer shares;

    private Boolean anonymity;


    public Question(User author, String text, String topic, String city, boolean hidden, Boolean anonymity) {
        this.author = author;
        this.text = text;
        this.topic = topic;
        this.city = city;
        this.hidden = hidden;
        this.anonymity = anonymity;
    }

    public Question() {

    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Boolean getHidden() {
        return hidden;
    }

    public void setHidden(Boolean hidden) {
        this.hidden = hidden;
    }

    public void setAuthor(Optional<User> user) {
        user.ifPresent(value -> this.author = value);
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer comments) {
        this.commentCount = comments;
    }

    public Integer getShares() {
        return shares;
    }

    public void setShares(Integer shares) {
        this.shares = shares;
    }

    public Boolean getAnonymity() {
        return anonymity;
    }

    public void setAnonymity(Boolean anonymity) {
        this.anonymity = anonymity;
    }

    public void incrementLike() {
        if (this.likes == null) {
            this.likes = 1;
        }else {
            this.likes++;
        }
    }

    public void decrementLike() {
        if (this.likes == null) {
            this.likes = 0;
        }else {
            this.likes--;
        }
    }
}
