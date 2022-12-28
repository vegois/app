package com.example.application.views.questionlist;

import com.example.application.data.entity.Foto;
import com.example.application.data.entity.Question;
import com.example.application.data.service.imp.ImageService;
import com.example.application.data.service.imp.QuestionService;
import com.example.application.data.service.imp.PersonService;
import com.example.application.data.service.imp.UserService;
import com.example.application.views.MainLayout;
import com.example.application.views.card.CardView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

@PageTitle("Card List")
@Route(value = "card-list", layout = MainLayout.class)
@AnonymousAllowed
public class QuestionListView extends Div implements AfterNavigationObserver {

    @Autowired
    private QuestionService questionService;
    @Autowired
    private UserService userService;
    @Autowired
    private PersonService personService;

    Grid<QuestionDTO> grid = new Grid<>();
    private VerticalLayout imageContainer;
    private final ImageService imageService;


    public QuestionListView(ImageService imageService) {
        this.imageService = imageService;
        addClassName("card-list-view");
        setSizeFull();
        grid.setHeight("100%");
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS);
        grid.addComponentColumn(this::createCard);
        add(grid);
        grid.addSelectionListener(sel -> {
            if (sel.getFirstSelectedItem().isPresent()){
                UI.getCurrent().navigate(CardView.class, sel.getFirstSelectedItem().get().getId().toString());
            }
        });
    }

    private HorizontalLayout createCard(QuestionDTO questionDTO) {
        HorizontalLayout card = new HorizontalLayout();
        card.addClassName("question");
        card.setSpacing(false);
        card.getThemeList().add("spacing-s");

        Image image = convertSinglePhoto(questionDTO.getId());
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
        Span likes = new Span(questionDTO.getLikes() != null ?questionDTO.getLikes().toString() : "0");
        likes.addClassName("likes");
        Icon commentIcon = VaadinIcon.COMMENT.create();
        commentIcon.addClassName("icon");
        Span comments = new Span(questionDTO.getComments() != null ?questionDTO.getComments().toString() : "0");
        comments.addClassName("comments");
        Icon shareIcon = VaadinIcon.CONNECT.create();
        shareIcon.addClassName("icon");
        Span shares = new Span(questionDTO.getShares() != null ? questionDTO.getShares().toString() : "0");
        shares.addClassName("shares");

        actions.add(likeIcon, likes, commentIcon, comments, shareIcon, shares);
        description.add(header, post, actions);
        card.add(image, description);
        return card;
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        /*
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserInfo localUser = new UserInfo(auth.getName());
        User user = userService.findByName(localUser.getId());
        Player person = personService.findByUserName(user.getUsername());

         */
        List<Question> questionList = questionService.findALlByCity("Vienna")
        .stream().sorted((f1, f2) -> Long.compare(f2.getCreationDate().getTime(), f1.getCreationDate().getTime()))
                .toList();

        List<QuestionDTO> questionDTOList = new ArrayList<>();
        questionList.forEach(msg -> {
            questionDTOList.add(createPerson(msg.getId(), msg.getAuthor().getName(), msg.getCreationDate().toString(),
                    msg.getText(),
                    msg.getLikes(), msg.getCommentCount(), msg.getShares()));
        });
        grid.setItems(questionDTOList);
    }

    private static QuestionDTO createPerson(Long id, String name, String date, String post, Integer likes,
                                            Integer comments, Integer shares) {
        QuestionDTO p = new QuestionDTO();
        p.setId(id);
        p.setName(name);
        p.setDate(date);
        p.setPost(post);
        p.setLikes(likes);
        p.setComments(comments);
        p.setShares(shares);

        return p;
    }

    public Image convertSinglePhoto(Long id){
        StreamResource sr = new StreamResource("user", () -> {
            Foto attached = imageService.findByQuestionId(id);
            if (attached != null) {
                return new ByteArrayInputStream(attached.getPic());
            }
            return null;
        });
        sr.setContentType("image/png");
        return new Image(sr, "profile-picture");
    }

}
