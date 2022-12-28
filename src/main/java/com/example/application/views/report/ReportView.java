package com.example.application.views.report;

import com.example.application.data.entity.Foto;
import com.example.application.data.entity.Player;
import com.example.application.data.entity.Question;
import com.example.application.data.entity.User;
import com.example.application.data.service.imp.ImageService;
import com.example.application.data.service.imp.QuestionService;
import com.example.application.data.service.imp.UserService;
import com.example.application.views.MainLayout;
import com.vaadin.collaborationengine.UserInfo;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import javax.annotation.security.RolesAllowed;
import javax.imageio.ImageIO;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;

@PageTitle("Report/Ask")
@Route(value = "report", layout = MainLayout.class)
@Uses(Icon.class)
@RolesAllowed("ADMIN")
public class ReportView extends Div {

    private TextField city = new TextField("City");

    private TextField postalCode = new TextField("postalCode");

    private TextField image = new TextField("Image");
    private TextField topic = new TextField("Topic");

    private TextField category = new TextField("Category");
    private TextField post = new TextField("Post");

    private Button cancel = new Button("Cancel");
    private Button save = new Button("Save");

    private Binder<Question> binder = new Binder<>(Question.class);

    private VerticalLayout imageContainer;
    private final ImageService imageService;
    private final UserService userService;
    private Player player;
    private Foto foto;

    public ReportView(QuestionService questionService, UserService userService, ImageService imageService, UserService userService1) {
        this.imageService = imageService;
        this.userService = userService1;
        addClassName("report-form-view");

        add(createTitle());
        add(createFormLayout());
        add(createButtonLayout());
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserInfo localUser = new UserInfo(auth.getName());

        binder.bindInstanceFields(this);
        initUploaderImage();
        clearForm();


        cancel.addClickListener(e -> clearForm());
        save.addClickListener(e -> {
            Question question = binder.getBean();
            User user = userService.findByName(localUser.getId());
            question.setAuthor(user);
            question.setHidden(false);
            question.setUpdatedDate(new Date());
            question.setText(post.getValue());
            question = questionService.update(binder.getBean());

            foto.setReportId(question.getId());
            imageService.save(foto);
            Notification.show(question.getText() + " ->  details stored.");
            clearForm();
        });
    }

    public void initUploaderImage() {
        MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
        Upload upload = new Upload(buffer);
        upload.setAcceptedFileTypes("image/jpeg","image/jpg", "image/png", "image/gif");

        upload.addSucceededListener(event -> {
            String attachmentName = event.getFileName();
            try {
                // The image can be jpg png or gif, but we store it always as png file in this example
                BufferedImage inputImage = ImageIO.read(buffer.getInputStream(attachmentName));
                ByteArrayOutputStream pngContent = new ByteArrayOutputStream();
                ImageIO.write(inputImage, "png", pngContent);
                saveProfilePicture(pngContent.toByteArray());
                showImage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        add(upload);
        initImageContainer();
    }

    public void saveProfilePicture(byte[] imageBytes) {
        foto = new Foto();
        foto.setPic(imageBytes);
        foto = imageService.save(foto);
    }

    public void showImage() {
        if (this.player != null ) {
            Image image = imageService.generateImage(player.getId());
            image.setHeight("100%");
            imageContainer.removeAll();
            imageContainer.add(image);
        }
    }

    private void initImageContainer(){
        imageContainer = new VerticalLayout();
        imageContainer.setWidth("200px");
        imageContainer.setHeight("200px");
        imageContainer.getStyle().set("overflow-x", "auto");
        add(imageContainer);
    }

    private void clearForm() {
        binder.setBean(new Question());
    }

    private Component createTitle() {
        return new H3("Personal information");
    }

    private Component createFormLayout() {
        FormLayout formLayout = new FormLayout();
        formLayout.add(city, postalCode, image, topic, category, post);
        return formLayout;
    }

    private Component createButtonLayout() {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.addClassName("button-layout");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(save);
        buttonLayout.add(cancel);
        return buttonLayout;
    }
}
