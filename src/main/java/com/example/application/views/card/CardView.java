package com.example.application.views.card;

import com.example.application.data.entity.*;
import com.example.application.data.service.imp.*;
import com.example.application.views.MainLayout;
import com.example.application.views.questionlist.CommentDTO;
import com.example.application.views.questionlist.QuestionDTO;
import com.vaadin.collaborationengine.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.messages.MessageInput;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.StreamResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.annotation.security.PermitAll;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@PageTitle("Neighbor Question")
@Route(value = "neighbor", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
@PermitAll
public class CardView extends Div implements HasUrlParameter<String> {
    @Autowired
    private MyMessagePersister messagePersister;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private CommentService commentService;
    private QuestionDTO questionDTO;
    private Question question;
    private String questionId;

    private HorizontalLayout imageContainer;
    private final ImageService imageService;
    private final UserService userService;
    private final LikesService likesService;
    private final NotificationService notificationService;
    private User user;
    private UserInfo localUser;

    Grid<QuestionDTO> grid = new Grid<>();
    Grid<CommentDTO> commentGrid = new Grid<>();

    public CardView(ImageService imageService, UserService userService, LikesService likesService, NotificationService notificationService) {
        this.imageService = imageService;
        this.userService = userService;
        this.likesService = likesService;
        this.notificationService = notificationService;
        addClassName("card-view");
        setSizeFull();
    }

    private void loadCardView() {
        if (this.questionDTO != null) {
            List<QuestionDTO> questionDTOList = new ArrayList<>();
            questionDTOList.add(questionDTO);
            grid.setItems(questionDTOList);
            grid.addComponentColumn(this::createCard);
            add(grid);
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            this.localUser = new UserInfo(auth.getName());
            String topicId = "notifications";
            /*
            MessageManager messageManager = new MessageManager(this, localUser,
                    topicId);

            messageManager.setMessageHandler(context -> {
                CollaborationMessage message = context.getMessage();
                UserInfo user = message.getUser();
                String text = message.getText();

                if (StringUtils.isNotBlank(message.getText())) {
                    messagePersister.persistComment(message, questionDTO.getId());
                }
                message = new CollaborationMessage();
                Notification.show(user.getName() + ": " + text);
            });

             */

            Checkbox checkbox = new Checkbox();
            checkbox.setLabel("Anonymous");
            add(checkbox);
            add(new MessageInput(event -> {
                String text = event.getValue();
                if (StringUtils.isNotBlank(text)) {
                    CollaborationMessage message = new CollaborationMessage(localUser, text, Instant.now());
                    Question questionEntity = questionService.findById(questionDTO.getId());
                    user = userService.findByName(localUser.getId());
                    Comment comment = new Comment(questionEntity,
                            user,
                            message.getText(),
                            questionEntity.getTopic(),
                            questionEntity.getCity(),
                            false,
                            questionEntity.getCategory(),
                            Instant.now(),
                            0,0,
                            checkbox.getValue());
                    messagePersister.persistComment(comment);
                }
                Notification.show(localUser.getId() + ": " + text);
                remove(commentGrid);
                loadComments();
                add(commentGrid);
            }));
            loadComments();
        }
        add(commentGrid);
    }

    private void loadComments() {
        if (questionDTO!= null){
            commentGrid = new Grid<>();
            List<Comment> commentList = commentService.findByQuestionId(questionDTO.getId());
            List<CommentDTO> commentDTOList = new ArrayList<>();
            for (Comment comment : commentList) {
                CommentDTO commentDTO = new CommentDTO(
                        comment.getId(),
                        comment.getAnonymity() != null && comment.getAnonymity() ? "Anonymous" : comment.getAuthor().getUsername(),
                        comment.getCreationDate().toString(), comment.getText(), comment.getLikes(),
                        comment.getCommentCount(), 0);
                commentDTOList.add(commentDTO);
            }
            commentGrid.setItems(commentDTOList);
            commentGrid.addComponentColumn(this::createCommentCard);
        }
    }

    private void loadQuestion(String questionId) {
        if (StringUtils.isNotBlank(questionId)){
            this.question = questionService.findById(Long.parseLong(questionId));
            this.questionDTO = new QuestionDTO(
                    question.getId(),
                    question.getAnonymity() != null && question.getAnonymity() ? "Anonymous" : question.getAuthor().getUsername(),
                    question.getUpdatedDate() != null ? question.getUpdatedDate().toString() : "",
                    question.getText(), question.getLikes(),
                    question.getCommentCount(), question.getShares()
            );
        }
    }

    private VerticalLayout createCard(QuestionDTO questionDTO) {

        VerticalLayout card = new VerticalLayout();
        card.addClassName("question");
        card.setSpacing(false);
        card.getThemeList().add("spacing-s");
        Button button = new Button("Notify me");
        button.addClickListener(e -> {
            if (localUser != null){
                user = userService.findByUserName(localUser.getId());

                com.example.application.data.entity.Notification notification;
                notification = notificationService.findByUsernameAndReportId(user.getUsername(), questionDTO.getId());
                if (notification != null){
                    notificationService.delete(notification);
                    return;
                }
                notification = new com.example.application.data.entity.Notification();
                notification.setUsername(user.getUsername());
                notification.setReportId(questionDTO.getId());

                notificationService.save(notification);

            }
        });
        card.add(button);
        Image image = convertSinglePhoto();
        VerticalLayout description = getVerticalLayout(questionDTO);
        showImage(card);
        card.add(image, description);

        return card;
    }

    private VerticalLayout createCommentCard(QuestionDTO questionDTO1) {
        VerticalLayout card = new VerticalLayout();
        card.addClassName("question");
        card.setSpacing(false);
        card.getThemeList().add("spacing-s");

        Image image = new Image();
        VerticalLayout description = getVerticalLayout(questionDTO1);
        card.add(image, description);
        return card;
    }

    private VerticalLayout getVerticalLayout(QuestionDTO questionDTO) {
        VerticalLayout description = new VerticalLayout();
        description.addClassName("description");
        description.setSpacing(false);
        description.setPadding(false);

        HorizontalLayout header = new HorizontalLayout();
        header.addClassName("header");
        header.setSpacing(false);
        header.getThemeList().add("spacing-s");

        Span name = new Span(questionDTO.getName());
        name.addClassName("name");
        Span date = new Span(questionDTO.getDate());
        date.addClassName("date");
        header.add(name, date);

        Span post = new Span(questionDTO.getPost());
        post.addClassName("post");

        HorizontalLayout actions = new HorizontalLayout();
        actions.addClassName("actions");
        actions.setSpacing(false);
        actions.getThemeList().add("spacing-s");

        Icon likeIcon = VaadinIcon.HEART.create();
        likeIcon.addClassName("icon");
        if (localUser != null){
            Likes like = likesService.findByUsernameAndReportId(localUser.getId(), questionDTO.getId());
            if (like != null){
                likeIcon.setColor("red");
            }else{
                likeIcon.setColor("gray");
            }
        }
        Span likes = new Span(questionDTO.getLikes() != null ? questionDTO.getLikes().toString() : "0");
        likes.addClassName("likes");

        likeIcon.addClickListener(e ->{
           if (localUser != null){
               Likes like = likesService.findByUsernameAndReportId(localUser.getId(), questionDTO.getId());
               if (like != null){
                   likesService.delete(like);
                   question.decrementLike();
                   question = questionService.update(question);
                   likeIcon.setColor("gray");
               }else{
                   like = new Likes(localUser.getId(), questionDTO.getId(), null);
                   likesService.save(like);
                   question.incrementLike();
                   question = questionService.update(question);
                   likeIcon.setColor("red");
               }
           }
        });
        Icon commentIcon = VaadinIcon.COMMENT.create();
        commentIcon.addClassName("icon");
        Span comments = new Span(questionDTO.getComments() != null ? questionDTO.getComments().toString() : "0");
        comments.addClassName("comments");
        Icon shareIcon = VaadinIcon.CONNECT.create();
        shareIcon.addClassName("icon");
        Span shares = new Span(questionDTO.getShares() != null ? questionDTO.getShares().toString() : "0");
        shares.addClassName("shares");

        actions.add(likeIcon, likes, commentIcon, comments, shareIcon, shares);

        description.add(header, post, actions);
        return description;
    }


    @Override
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter String aLong) {
        if (StringUtils.isNotBlank(aLong)){
            this.questionId = aLong;
            if(StringUtils.isNotBlank(this.questionId)){
                loadQuestion(this.questionId);
            }
            loadCardView();
        }
    }

    public void showImage(VerticalLayout card) {

        if (this.question != null ) {
            if (imageContainer != null){
                imageContainer.removeAll();
            }
            imageContainer = new HorizontalLayout();
            initImageContainer();
            Image image = convertToImage();
            if (image != null){
                imageContainer.removeAll();
                imageContainer.add(image);
                card.add(imageContainer);
            }
        }
    }

    private void initImageContainer(){
        imageContainer.setMaxHeight("20px");
        imageContainer.setMaxWidth("20px");
        imageContainer.getStyle().set("overflow-x", "auto");
    }

    public Image convertToImage(){
        List<Foto> attached = imageService.findListByQuestionId(question.getId());
        List<ByteArrayInputStream> streams = new ArrayList<>();
        StreamResource sr = new StreamResource("user", () -> {
            if (attached != null) {
                for (Foto f: attached) {
                    streams.add(new ByteArrayInputStream(f.getPic()));
                }
            }
            return (InputStream) streams;
        });
        sr.setContentType("image/png");
        return new Image(sr, "profile-picture");
    }

    public Image convertSinglePhoto(){
        StreamResource sr = new StreamResource("user", () -> {
            Foto attached = imageService.findByQuestionId(question.getId());
            if (attached != null) {
                return new ByteArrayInputStream(attached.getPic());
            }
            return null;
        });
        sr.setContentType("image/png");
        return new Image(sr, "profile-picture");
    }


}