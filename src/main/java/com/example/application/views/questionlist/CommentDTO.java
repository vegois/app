package com.example.application.views.questionlist;

public class CommentDTO extends QuestionDTO{
    public CommentDTO(Long id, String name, String date, String post, Integer likes, Integer comments, Integer shares) {
        super(id, name, date, post, likes, comments, shares);
    }
}
