package com.example.application.views.authaccess.dictionaries.users;

import com.example.application.data.Status;
import com.example.application.entities.User;
import com.example.application.views.components.ErrorNotification;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UserFormView extends FormLayout {
    VerticalLayout fieldsLayout = new VerticalLayout();
    TextField username = new TextField();
    EmailField email = new EmailField();
    PasswordField password = new PasswordField();
    ComboBox<Status> status = new ComboBox<>();
    Checkbox active = new Checkbox();
    User user = new User();
    HorizontalLayout buttonsLayout = new HorizontalLayout();
    Button createButton = new Button();
    Button updateButton = new Button();
    Button deleteButton = new Button();

    Binder<User> userBinder = new BeanValidationBinder<>(User.class);

    public UserFormView() {
        userBinder.bindInstanceFields(this);
        allConfig();

        add(
                fieldsLayout,
                buttonsLayout
        );
    }

    public void setUser(User user) {
        this.user = user;
        userBinder.readBean(this.user);
    }

    //Config
    public void allConfig() {
        thisConfig();
        fieldsLayoutConfig();
        usernameConfig();
        emailConfig();
        passwordConfig();
        statusConfig();
        activeConfig();
        buttonLayoutConfig();
        createButtonConfig();
        updateButtonConfig();
        deleteButtonConfig();
    }

	public void thisConfig() {
        addClassName("dictionaryForm");

    }

    public void fieldsLayoutConfig() {
        fieldsLayout.setSizeFull();

        fieldsLayout.add(
                username,
                email,
                password,
                status
        );
    }

    public void usernameConfig() {
        username.setLabel("Имя пользователя");
        username.setWidthFull();
    }

    public void emailConfig() {
        email.setLabel("Email");
        email.setWidthFull();
    }

    public void passwordConfig() {
        password.setLabel("Пароль");
        password.setWidthFull();
    }

    public void statusConfig() {
        status.setLabel("Статус");
        status.setItems(Stream.of(
                Status.values()
        ).collect(Collectors.toList()));
        status.addValueChangeListener(event -> active.setValue(Status.enumToBool(status.getValue())));
        status.setWidthFull();
        status.setRequired(true);
    }

	private void activeConfig() {
    	active.setVisible(false);
	}

    public void buttonLayoutConfig() {
        buttonsLayout.setSizeFull();
        buttonsLayout.setAlignItems(FlexComponent.Alignment.START);
        buttonsLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.BASELINE);

        buttonsLayout.add(
                createButton,
                updateButton,
                deleteButton
        );
    }

    //Actions

    public void createButtonConfig() {
        createButton.setText("Создать");
        createButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        createButton.addClickListener(this::createButtonHandler);
    }

    private void createButtonHandler(ClickEvent<Button> buttonClickEvent) {
        trySave();
    }

    public void updateButtonConfig() {
        updateButton.setText("Обновить");
        updateButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        updateButton.addClickListener(this::updateButtonHandler);
    }

    private void updateButtonHandler(ClickEvent<Button> buttonClickEvent) {

    }

    public void deleteButtonConfig() {
        deleteButton.setText("Удалить");
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        deleteButton.addClickListener(this::deleteButtonHandler);
    }

    private void deleteButtonHandler(ClickEvent<Button> buttonClickEvent) {

    }

    //Validation

    private void trySave() {
        try {
            userBinder.writeBean(this.user);

            if(additionalValidate()) {
                System.out.println("Save User: " + this.user.toString());
            } else {

            }
        } catch (ValidationException e) {
			ErrorNotification.showNotification("Ошибка валидации формы!", true);
            e.printStackTrace();
        }
    }

    private boolean additionalValidate() {
        return statusValidate();
    }

    private boolean statusValidate() {
        if (status.isEmpty()) {
            status.setInvalid(true);
            ErrorNotification.showNotification("Статус не заполнен!", true);
        }

        return !status.isEmpty();
    }
}
