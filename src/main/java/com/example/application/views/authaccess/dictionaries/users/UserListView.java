package com.example.application.views.authaccess.dictionaries.users;

import com.example.application.entities.User;
import com.vaadin.flow.component.ClickEvent;
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

@Route("/users")
@PageTitle("Users dictionary")
@PermitAll
public class UserListView extends VerticalLayout {
	HorizontalLayout actionbar = new HorizontalLayout();
	TextField filterTextField = new TextField();
	Button actionButton = new Button();
	Grid<User> userGrid = new Grid<>(User.class);

	public UserListView() {
		allConfig();

		add(
		  actionbar,
		  userGrid
		);
	}

	public void allConfig() {
		thisConfig();
		userGridConfig();
		actionbarConfig();
		actionButtonConfig();
		filterTextFieldConfig();
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
		userGrid.addColumn(user -> user.isActive()).setHeader("Активность");
		userGrid.getColumns().forEach(userColumn -> userColumn.setAutoWidth(true).setSortable(true));
	}

	public void filterTextFieldConfig() {
		filterTextField.setLabel("Поиск пользователей");
		filterTextField.setPlaceholder("Введите текст...");
		filterTextField.setValueChangeMode(ValueChangeMode.LAZY);
	}

	private void buttonHandler(ClickEvent<Button> buttonClickEvent) {

	}
}
