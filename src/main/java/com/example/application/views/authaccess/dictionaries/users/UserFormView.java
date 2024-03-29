package com.example.application.views.authaccess.dictionaries.users;

import com.example.application.data.Status;
import com.example.application.entities.User;
import com.example.application.services.UserService;
import com.example.application.views.components.notification.ErrorNotification;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collections;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UserFormView extends FormLayout {
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	private VerticalLayout fieldsLayout = new VerticalLayout();
	TextField username = new TextField();
	EmailField email = new EmailField();
	PasswordField password = new PasswordField();
	ComboBox<Status> status = new ComboBox<>();
	private Checkbox active = new Checkbox();
	private User user = new User();
	HorizontalLayout buttonsLayout = new HorizontalLayout();
	private Button createButton = new Button();
	private Button updateButton = new Button();
	private Button deleteButton = new Button();
	Button manageRoles = new Button();
	private final UserService userService;
	private Icon closeButton;
	HorizontalLayout closeButtonContainer = new HorizontalLayout();

	Consumer createButtonEvent;
	Consumer updateButtonEvent;
	Consumer deleteButtonEvent;
	Consumer closeFormEvent;
	Consumer manageRoleEvent;

	Binder<User> userBinder = new BeanValidationBinder<>(User.class);

	public UserFormView(UserService userService, int BCRYPT_STRENGTH) {
		this.bCryptPasswordEncoder = new BCryptPasswordEncoder(BCRYPT_STRENGTH);
		this.userService = userService;
		userBinder.bindInstanceFields(this);
		allConfig();

		add(
		  closeButtonContainer,
		  fieldsLayout,
		  buttonsLayout,
		  manageRoles
		);
	}

	public void setUser(User user) {
		this.user = user;
		userBinder.readBean(this.user);
		setUserConfigFields();
	}

	public void setUserConfigFields() {
		status.setValue(Status.boolToEnum(active.getValue()));
	}

	public void setManageRolesButtonEnabled(boolean val) {
		this.manageRoles.setEnabled(val);
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
		manageRolesConfig();
		closeButtonConfig();
		closeButtonContainerConfig();
	}

	public void thisConfig() {
		addClassName("dictionaryForm");
	}

	public void closeButtonConfig() {
		closeButton = new Icon(VaadinIcon.CLOSE_CIRCLE);
		closeButton.addClassName("closeButtonConfig");
		closeButton.setColor("hsla(21hsla(214, 50%, 22%, 0.26)");
		closeButton.addClickListener(this::closeFormHandler);
	}

	private void closeFormHandler(ClickEvent<Icon> iconClickEvent) {
		closeFormEvent.accept(iconClickEvent);
	}

	public void closeButtonContainerConfig() {
		closeButtonContainer.addClassName("closeButtonContainer");
		closeButtonContainer.addClassName("pt-m");
		closeButtonContainer.setWidthFull();
		closeButtonContainer.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
		closeButtonContainer.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.START);
		closeButtonContainer.add(
		  closeButton
		);
	}

	public void manageRolesConfig() {
		manageRoles.addClassName("manageRoles");
		manageRoles.setText("Роли пользователя");
		manageRoles.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		manageRoles.setSizeFull();
		manageRoles.setEnabled(false);
		manageRoles.addClickListener(this::manageRolesHandler);
	}

	private void manageRolesHandler(ClickEvent<Button> buttonClickEvent) {
		manageRoleEvent.accept(buttonClickEvent);
	}

	public void fieldsLayoutConfig() {
		fieldsLayout.setSizeFull();
		fieldsLayout.setSpacing(false);
		fieldsLayout.setPadding(false);

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
		buttonsLayout.setClassName("actionButtonLayout");
		buttonsLayout.setSizeFull();
		buttonsLayout.setAlignItems(FlexComponent.Alignment.START);
		buttonsLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.BASELINE);
		fieldsLayout.setSpacing(false);
		fieldsLayout.setPadding(false);

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
		if (trySave()) createButtonEvent.accept(buttonClickEvent);
	}

	public void updateButtonConfig() {
		updateButton.setText("Обновить");
//        updateButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		updateButton.addClickListener(this::updateButtonHandler);
	}

	private void updateButtonHandler(ClickEvent<Button> buttonClickEvent) {
		if (tryUpdate()) updateButtonEvent.accept(buttonClickEvent);
	}

	public void deleteButtonConfig() {
		deleteButton.setText("Удалить");
		deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_PRIMARY);
		deleteButton.addClickListener(this::deleteButtonHandler);
	}

	private void deleteButtonHandler(ClickEvent<Button> buttonClickEvent) {
		if (tryDelete())
			deleteButtonEvent.accept(buttonClickEvent);
	}

	private boolean tryDelete() {
		try {
			if (!this.userService.delete(this.user)) {
				throw new Exception(
				  "Ошибка удаления пользователя!"
				);
			} else {
				ErrorNotification.showNotification(
				  "Пользователь успешно удален!", false
				);
				fieldsToDefault();
				return true;
			}
		} catch (Exception e) {
			ErrorNotification.showNotification("Ошибка формы: " + e.getMessage(), true);
		}

		return false;
	}

	//DB operations
	private boolean trySave() {
		try {
			userBinder.writeBean(this.user);
			createNewUserForSave();

			if (this.userService.save(this.user) == null) {
				throw new Exception(
				  "Ошибка сохранения пользователя!\n Email или Логин пользователя не уникальны!"
				);
			} else {
				ErrorNotification.showNotification(
				  "Пользователь успешно сохранен!\n Добавьте пользователю роли.", false
				);
				fieldsToDefault();
				return true;
			}
		} catch (Exception e) {
			ErrorNotification.showNotification("Ошибка формы: " + e.getMessage(), true);
		}

		return false;
	}

	//DB operations
	private boolean tryUpdate() {
		try {
			userBinder.writeBean(this.user);
			if (
			  !this.userService.findById(this.user.getId()).stream().findFirst().isEmpty()
				&& !this.userService.findById(this.user.getId()).stream().findFirst().get().getPassword().equals(password.getValue())
			) this.user.setPassword(bCryptPasswordEncoder.encode(password.getValue()));

			if (this.userService.resultUserAlreadyExist(this.user)) {
				throw new Exception(
				  "Ошибка обновления пользователя!\n Уже существует пользователь с таким Email или Логин!"
				);
			} else {
				this.userService.update(this.user);
				ErrorNotification.showNotification(
				  "Пользователь успешно обновлен!", false
				);
				fieldsToDefault();
				return true;
			}
		} catch (Exception e) {
			ErrorNotification.showNotification("Ошибка формы: " + e.getMessage(), true);
		} finally {
			password.setValue("");
			password.setInvalid(false);
		}

		return false;
	}

	private void createNewUserForSave() {
		this.user = new User(
		  this.user.getUsername(),
		  this.user.getEmail(),
		  bCryptPasswordEncoder.encode(password.getValue()),
		  this.user.isActive(),
		  Collections.emptyList()
		);
	}

	//Additional methods
	public void fieldsToDefault() {
		clearFields();
		unInvalidateFields();
	}

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
	public void createButtonEvent(Consumer callback) {
		this.createButtonEvent = callback;
	}

	public void updateButtonEvent(Consumer callback) {
		this.updateButtonEvent = callback;
	}

	public void deleteButtonEvent(Consumer callback) {
		this.deleteButtonEvent = callback;
	}

	public void closeFormEvent(Consumer callback) {
		this.closeFormEvent = callback;
	}

	public void manageRoleEvent(Consumer callback) {
		this.manageRoleEvent = callback;
	}
}
