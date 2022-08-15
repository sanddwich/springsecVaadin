package com.example.application.views.authaccess.dictionaries.users;

import com.example.application.data.Status;
import com.example.application.entities.User;
import com.example.application.services.UserService;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.security.PermitAll;
import java.util.List;

@Route("/users")
@PageTitle("Users dictionary")
@PermitAll
public class UserListView extends VerticalLayout {
	private HorizontalLayout actionbar = new HorizontalLayout();
	private HorizontalLayout content = new HorizontalLayout();
	private TextField filterTextField = new TextField();
	private Button actionButton = new Button();
	private Grid<User> userGrid = new Grid<>(User.class);
	private UserFormView userFormView;
	private final UserService userService;
	private final String editClassName = "editing";

	public UserListView(UserService userService) {
		this.userService = userService;
		this.userFormView = new UserFormView(userService);

		allConfig();
		closeUserForm();

		add(
		  actionbar,
		  content
		);

	}

	public void closeUserForm() {
		userFormView.setVisible(false);
		userFormView.setUser(null);
		removeClassName(editClassName);
	}

	public void updateUserList() {
		List<User> userList = filterTextField.getValue().isEmpty()
				? userService.findAll()
				: userService.search(filterTextField.getValue());

		actionButton.setEnabled(userList.isEmpty());
		userGrid.setItems(userList);
	}

	public void allConfig() {
		thisConfig();
		userGridConfig();
		actionbarConfig();
		actionButtonConfig();
		filterTextFieldConfig();
		userFormViewConfig();
		contentConfig();
	}

	public void contentConfig() {
		content.setClassName("content");
		content.setFlexGrow(2, userGrid);
		content.setFlexGrow(1, userFormView);
		content.setSizeFull();

		content.add(
				userGrid,
				userFormView
		);
	}

	private void userFormViewConfig() {
		userFormView.formButtonClickEvent(event -> formButtonClickEvent());
		userFormView.setWidth("25em");
	}

	public void thisConfig() {
		addClassName("dictionaryView");
		setSizeFull();
	}

	public void actionbarConfig() {
		actionbar.setWidthFull();
		actionbar.setAlignItems(Alignment.START);
		actionbar.setDefaultVerticalComponentAlignment(Alignment.BASELINE);
		actionbar.add(filterTextField, actionButton);
	}

	public void actionButtonConfig() {
		actionButton.setText("Добавить");
		actionButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		actionButton.addClickListener(this::buttonHandler);
	}

	public void userGridConfig() {
		userGrid.setClassName("grid");
		userGrid.setSizeFull();
//		userGrid.setColumns("username", "email", "active");
		userGrid.removeAllColumns();
		userGrid.addColumn(user -> user.getUsername()).setHeader("Имя пользователя");
		userGrid.addColumn(user -> user.getEmail()).setHeader("email");
		userGrid.addColumn(user -> Status.boolToEnum(user.isActive())).setHeader("Активность");
		userGrid.getColumns().forEach(userColumn -> userColumn.setAutoWidth(true).setSortable(true));

		userGrid.asSingleSelect().addValueChangeListener(event -> userEdit(event.getValue()));

		updateUserList();
	}

	private void userEdit(User user) {
		if (user == null) {
			closeUserForm();
		} else {
			userFormView.setUser(user);
			userFormView.setVisible(true);
			addClassName(editClassName);
		}
	}

	public void filterTextFieldConfig() {
		filterTextField.setLabel("Поиск пользователей");
		filterTextField.setPlaceholder("Введите текст...");
		filterTextField.setClearButtonVisible(true);
		filterTextField.setValueChangeMode(ValueChangeMode.LAZY);
		filterTextField.addValueChangeListener(textFieldStringComponentValueChangeEvent -> updateUserList());
		filterTextField.addValueChangeListener(event -> {
			if (filterTextField.getValue().isEmpty())
				closeUserForm();
		});
	}

	private void buttonHandler(ClickEvent<Button> buttonClickEvent) {
		User newUser = new User();
		newUser.setUsername(filterTextField.getValue());
		newUser.setEmail(filterTextField.getValue());
		userEdit(newUser);
	}

	public void formButtonClickEvent() {
//		filterTextField.setValue("");
		updateUserList();
	}
}
