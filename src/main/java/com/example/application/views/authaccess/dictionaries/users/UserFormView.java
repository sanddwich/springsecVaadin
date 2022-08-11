package com.example.application.views.authaccess.dictionaries.users;

import com.example.application.data.Status;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UserFormView extends FormLayout {
    VerticalLayout fieldsLayout = new VerticalLayout();
    TextField username = new TextField();
    EmailField emailField = new EmailField();
    PasswordField passwordField = new PasswordField();
    ComboBox<Status> status = new ComboBox<>();

    HorizontalLayout buttonsLayout = new HorizontalLayout();
    Button createButton = new Button();
    Button updateButton = new Button();
    Button deleteButton = new Button();

    public UserFormView() {
        allConfig();

        add(
                fieldsLayout,
                buttonsLayout
        );
    }

    public void allConfig() {
        thisConfig();
        fieldsLayoutConfig();
        usernameConfig();
        emailFieldConfig();
        passwordFieldConfig();
        statusConfig();
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
                emailField,
                passwordField,
                status
        );
    }

    public void usernameConfig() {
        username.setLabel("Имя пользователя");
        username.setWidthFull();
    }

    public void emailFieldConfig() {
        emailField.setLabel("Email");
        emailField.setWidthFull();
    }

    public void passwordFieldConfig() {
        passwordField.setLabel("Пароль");
        passwordField.setWidthFull();
    }

    public void statusConfig() {
        status.setLabel("Статус");
        status.setItems(Stream.of(
                Status.ACTIVE, Status.NOACTIVE
        ).collect(Collectors.toList()));
        status.setWidthFull();
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

    public void createButtonConfig() {
        createButton.setText("Создать");
        createButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        createButton.addClickListener(this::createButtonHandler);
    }

    private void createButtonHandler(ClickEvent<Button> buttonClickEvent) {
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


}
