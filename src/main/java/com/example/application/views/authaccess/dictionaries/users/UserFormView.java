package com.example.application.views.authaccess.dictionaries.users;

import com.example.application.data.Status;
import com.example.application.entities.User;
import com.example.application.services.UserService;
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
import com.vaadin.flow.component.textfield.Autocomplete;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UserFormView extends FormLayout {
	private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
	private VerticalLayout fieldsLayout = new VerticalLayout();
	private TextField username = new TextField();
	private EmailField email = new EmailField();
	private PasswordField password = new PasswordField();
	private ComboBox<Status> status = new ComboBox<>();
	private Checkbox active = new Checkbox();
	private User user = new User();
	private HorizontalLayout buttonsLayout = new HorizontalLayout();
	private Button createButton = new Button();
	private Button updateButton = new Button();
	private Button deleteButton = new Button();
	private final UserService userService;

	Consumer formButtonClickEvent;

	Binder<User> userBinder = new BeanValidationBinder<>(User.class);

	public UserFormView(UserService userService) {
		this.userService = userService;
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
		setUserConfigFields();
	}

	public void setUserConfigFields() {
		password.setValue("");
		password.setInvalid(false);
		status.setValue(Status.boolToEnum(active.getValue()));
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
//		username.setAutocomplete(Autocomplete.OFF);
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

	//DB operations
	private boolean trySave() {
		try {
			userBinder.writeBean(this.user);
//			this.user.setPassword(bCryptPasswordEncoder.encode(this.user.getPassword()));


//			if (
//			  additionalValidate()
//				&& this.userService.save(this.user) != null
//			) {
//				ErrorNotification.showNotification(
//				  "Пользователь успешно сохранен!\n Добавьте пользователю роли.", false
//				);
//				clearFields();
//				unInvalidateFields();
//				return true;
//			} else {
//				ErrorNotification.showNotification(
//				  "Ошибка сохранения пользователя!\n Email или Логин пользователя не уникальны!", true
//				);
//				return false;
//			}
		} catch (Exception e) {
			ErrorNotification.showNotification("Ошибка валидации формы: " + e.getMessage(), true);
		}

		return false;
	}

	//Additional methods
	private void clearFields() {
		username.clear();
		email.clear();
		password.clear();
		status.clear();
		active.clear();
	}

	private void unInvalidateFields() {
		username.setInvalid(false);
		email.setInvalid(false);
		password.setInvalid(false);
		status.setInvalid(false);
	}

	//Consumer
	public void formButtonClickEvent(Consumer consumer) {
		this.formButtonClickEvent = consumer;
	}
}
