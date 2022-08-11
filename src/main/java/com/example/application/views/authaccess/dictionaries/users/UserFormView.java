package com.example.application.views.authaccess.dictionaries.users;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;

public class UserFormView extends FormLayout {
	TextField username = new TextField();
	EmailField emailField = new EmailField();
	PasswordField passwordField = new PasswordField();

	public UserFormView() {

	}
}
