package com.example.application.views.questionlist;

public class QuestionDTO {

    private Long id;

    private String name;
    private String date;
    private String post;
    private Integer likes;
    private Integer comments;
    private Integer shares;

    public QuestionDTO() {
    }

    public QuestionDTO(Long id, String name, String date, String post, Integer likes, Integer comments, Integer shares) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.post = post;
        this.likes = likes;
        this.comments = comments;
        this.shares = shares;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public Integer getComments() {
        return comments;
    }

    public void setComments(Integer comments) {
        this.comments = comments;
    }

    public Integer getShares() {
        return shares;
    }

    public void setShares(Integer shares) {
        this.shares = shares;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
