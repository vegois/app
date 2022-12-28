package com.example.application.views.personform;

import com.example.application.data.entity.Foto;
import com.example.application.data.entity.Player;
import com.example.application.data.entity.User;
import com.example.application.data.service.imp.ImageService;
import com.example.application.data.service.imp.PersonService;
import com.example.application.data.service.imp.UserService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.security.RolesAllowed;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@PageTitle("Person Form")
@Route(value = "person-form", layout = MainLayout.class)
@RolesAllowed("ADMIN")
@Uses(Icon.class)
public class PersonFormView extends Div {

    private TextField firstName = new TextField("First name");
    private TextField lastName = new TextField("Last name");

    private TextField userName = new TextField("Username");

    private EmailField email = new EmailField("Email address");

    private PasswordField password = new PasswordField("Password");

    private DatePicker dateOfBirth = new DatePicker("Birthday");
    private PhoneNumberField phone = new PhoneNumberField("Phone number");
    private TextField occupation = new TextField("Occupation");

    private TextField postalCode = new TextField("Postal code");
    private TextField city = new TextField("City");

    private Button cancel = new Button("Cancel");
    private Button save = new Button("Save");

    private Binder<Player> binder = new Binder<>(Player.class);
    private VerticalLayout imageContainer;
    private final ImageService imageService;
    private final UserService userService;
    private Player player;
    private Foto foto;


    public PersonFormView(PersonService personService, UserService userService, PasswordEncoder passwordEncoder, ImageService imageService) {
        this.imageService = imageService;
        this.userService = userService;
        addClassName("person-form-view");

        add(createTitle());
        add(createFormLayout());
        add(createButtonLayout());
        firstName.setRequired(true);
        firstName.setErrorMessage("First Name is mandatory!");
        lastName.setRequired(true);
        firstName.setErrorMessage("Last Name is mandatory!");

        userName.setRequired(true);
        userName.setErrorMessage("Username is mandatory and unique");
        password.setRequired(true);
        userName.setErrorMessage("Password is mandatory and unique");

        dateOfBirth.setRequired(true);
        city.setRequired(true);
        city.setRequiredIndicatorVisible(true);
        city.setErrorMessage("City is mandatory");

        email.setRequiredIndicatorVisible(true);
        initUploaderImage();

        binder.bindInstanceFields(this);
        clearForm();

        cancel.addClickListener(e -> clearForm());
        save.addClickListener(e -> {
            if (userService.findByUserName(userName.getValue()) != null){
                Notification.show("Username: " + userName.getValue() + " is taken!");

            }else {
                this.player = personService.update(binder.getBean());
                Notification.show(binder.getBean().getClass().getSimpleName() + " details stored.");
                User user = new User();
                user.setId(player.getId());
                user.setName(firstName.getValue());
                user.setUsername(userName.getValue());
                user.setHashedPassword(passwordEncoder.encode(password.getValue()));
                userService.update(user);
                foto.setPlayerId(player.getId());
                imageService.update(foto);
                clearForm();
                Notification.show("Person: " + firstName.getValue() + " stored!");
            }
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
        binder.setBean(new Player());
    }

    private Component createTitle() {
        return new H3("Personal information");
    }

    private Component createFormLayout() {
        FormLayout formLayout = new FormLayout();
        email.setErrorMessage("Please enter a valid email address");
        formLayout.add(firstName, lastName,userName, dateOfBirth, phone, email,password, occupation, postalCode, city);
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

    private static class PhoneNumberField extends CustomField<String> {
        private ComboBox<String> countryCode = new ComboBox<>();
        private TextField number = new TextField();

        public PhoneNumberField(String label) {
            setLabel(label);
            countryCode.setWidth("120px");
            countryCode.setPlaceholder("Country");
            countryCode.setAllowedCharPattern("[\\+\\d]");
            countryCode.setItems("+354", "+91", "+62", "+98", "+964", "+353", "+44", "+972", "+39", "+225");
            countryCode.addCustomValueSetListener(e -> countryCode.setValue(e.getDetail()));
            number.setAllowedCharPattern("\\d");
            HorizontalLayout layout = new HorizontalLayout(countryCode, number);
            layout.setFlexGrow(1.0, number);
            add(layout);
        }

        @Override
        protected String generateModelValue() {
            if (countryCode.getValue() != null && number.getValue() != null) {
                String s = countryCode.getValue() + " " + number.getValue();
                return s;
            }
            return "";
        }

        @Override
        protected void setPresentationValue(String phoneNumber) {
            String[] parts = phoneNumber != null ? phoneNumber.split(" ", 2) : new String[0];
            if (parts.length == 1) {
                countryCode.clear();
                number.setValue(parts[0]);
            } else if (parts.length == 2) {
                countryCode.setValue(parts[0]);
                number.setValue(parts[1]);
            } else {
                countryCode.clear();
                number.clear();
            }
        }
    }

}
