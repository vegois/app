package com.example.application.data.service.imp;

import com.example.application.data.entity.Comment;
import com.example.application.data.entity.Question;
import com.example.application.data.entity.User;
import com.vaadin.collaborationengine.CollaborationMessage;
import com.vaadin.collaborationengine.CollaborationMessagePersister;
import com.vaadin.collaborationengine.UserInfo;
import com.vaadin.flow.spring.annotation.SpringComponent;

import java.util.stream.Stream;

@SpringComponent
public class MyMessagePersister implements CollaborationMessagePersister {

    private final QuestionService questionService;
    private final UserService userService;

    private final CommentService commentService;

    public MyMessagePersister(QuestionService questionService,
                              UserService userService,
                              CommentService commentService) {
        this.questionService = questionService;
        this.userService = userService;
        this.commentService = commentService;
    }

    @Override
    public Stream<CollaborationMessage> fetchMessages(FetchQuery query) {
        return questionService
                .findAllByTopicSince(query.getTopicId(), query.getSince())
                .stream()
                .map(questionEntity -> {
                    User author = questionEntity.getAuthor();
                    UserInfo userInfo = new UserInfo(author.getId().toString(),
                            author.getName(), author.getImageUrl());

                    return new CollaborationMessage(userInfo,
                            questionEntity.getText(), questionEntity.getCreationDate().toInstant());
                });
    }

    @Override
    public void persistMessage(PersistRequest request) {
        CollaborationMessage message = request.getMessage();

        Question questionEntity = new Question();
        questionEntity.setTopic(request.getTopicId());
        questionEntity.setText(message.getText());
        questionEntity
                .setAuthor(userService.findByName(message.getUser().getId()));

        // Set the time from the message only as a fallback option if your
        // database can't automatically add an insertion timestamp:
        // messageEntity.setTime(message.getTime());

        questionService.save(questionEntity);
    }


    public void persistMessageTest(CollaborationMessage message, String topic ) {

        Question questionEntity = new Question();
        questionEntity.setTopic(topic);
        questionEntity.setText(message.getText());
        questionEntity
                .setAuthor(userService.findByName(message.getUser().getId()));

        // Set the time from the message only as a fallback option if your
        // database can't automatically add an insertion timestamp:
        // messageEntity.setTime(message.getTime());

        questionService.save(questionEntity);
    }

    public void persistComment(Comment comment ) {
        commentService.save(comment);
    }
}
