package com.example.application.views.authaccess.dictionaries.privileges;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

public class PrivilegesFormView extends VerticalLayout {
	public TextField name = createDefaultTextField("Имя привилегии");
	public TextField code = createDefaultTextField("Код привилегии");
	public TextField description = createDefaultTextField("Описание привилегии");
	HorizontalLayout actionButtonLayout = new HorizontalLayout();
	public Button addPrivilegeButton = new Button("Создать");
	public Button updatePrivilegeButton = new Button("Обновить");
	public Button deletePrivilegeButton = new Button("Удалить");
	HorizontalLayout header = new HorizontalLayout();
	public Icon closeIconButton;

	public PrivilegesFormView() {
		allConfig();
	}

	public void allConfig() {
		closeIconButtonConfig();
		headerConfig();
		addPrivilegeButtonConfig();
		updatePrivilegeButtonConfig();
		deletePrivilegeButtonConfig();
		actionButtonLayoutConfig();
		thisConfig();
	}

	public void thisConfig() {
		addClassName("PrivilegeFormView");
		addClassNames("pl-0", "pt-0");
		setSizeFull();
		add(
		  header,
		  name,
		  code,
		  description,
		  actionButtonLayout
		);
	}

	public void actionButtonLayoutConfig() {
		actionButtonLayout.setWidthFull();
		actionButtonLayout.setAlignItems(Alignment.CENTER);
		actionButtonLayout.setJustifyContentMode(JustifyContentMode.BETWEEN);
		actionButtonLayout.add(
		  addPrivilegeButton,
		  updatePrivilegeButton,
		  deletePrivilegeButton
		);
	}

	public void addPrivilegeButtonConfig() {
		addPrivilegeButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
	}

	public void updatePrivilegeButtonConfig() {
	}

	public void deletePrivilegeButtonConfig() {
		deletePrivilegeButton.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_PRIMARY);
	}

	public void headerConfig() {
		header.setSpacing(false);
		header.setPadding(false);
		header.addClassName("pt-m");
		header.setWidthFull();
		header.setJustifyContentMode(JustifyContentMode.END);
		header.setAlignItems(Alignment.CENTER);
		header.add(
		  closeIconButton
		);
	}

	public void closeIconButtonConfig() {
		closeIconButton = new Icon(VaadinIcon.CLOSE_CIRCLE);
		closeIconButton.addClassName("closeButtonConfig");
		closeIconButton.setColor("hsla(21hsla(214, 50%, 22%, 0.26)");
	}

	public TextField createDefaultTextField(String textFieldName) {
		TextField textField = new TextField(textFieldName);
		textField.setWidthFull();
		textField.addClassName("py-0");
		return textField;
	}

}
