package com.example.application.data.entity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.time.Instant;

@Entity
public class Comment extends AbstractEntity {

    @ManyToOne
    private Question question;
    @ManyToOne
    private User author;
    private String text;
    private String topic;

    private String city;
    private Boolean hidden;
    private String category;
    private Instant updatedDate;
    private Integer likes;
    private Integer commentCount;
    private Boolean anonymity;

    public Comment(Question question, User author, String text, String topic, String city, Boolean hidden, String category, Instant updatedDate, Integer likes, Integer commentCount, Boolean anonymity) {
        this.question = question;
        this.author = author;
        this.text = text;
        this.topic = topic;
        this.city = city;
        this.hidden = hidden;
        this.category = category;
        this.updatedDate = updatedDate;
        this.likes = likes;
        this.commentCount = commentCount;
        this.anonymity = anonymity;
    }

    public Comment() {

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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Instant getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Instant updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public Boolean getAnonymity() {
        return anonymity;
    }

    public void setAnonymity(Boolean anonymity) {
        this.anonymity = anonymity;
    }
}
