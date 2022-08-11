package com.example.application.views.authaccess.test;

import com.sun.tools.jconsole.JConsoleContext;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.security.PermitAll;

@Route("/example")
@PageTitle("Vaadin Example Page")
@PermitAll
public class ExampleClass extends VerticalLayout {
	HorizontalLayout actionbar = new HorizontalLayout();
	TextField textField = new TextField();
	Button button = new Button();

	public ExampleClass() {
		configAll();

		add(actionbar);
	}

	public void configAll() {
		configButton();
		configTextField();
		configActionbar();
	}

	public void configButton() {
		button.setText("Click Me");
		button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		button.addClickListener(this::buttonHandler);
	}

	private void buttonHandler(ClickEvent<Button> buttonClickEvent) {
		System.out.println("Button clicked! TextField contain: " + textField.getValue());
	}

	public void configTextField() {
		textField.setLabel("Text Field");
		textField.setPlaceholder("Enter text");
	}

	public void configActionbar() {
		actionbar.setWidthFull();
		actionbar.setAlignItems(Alignment.START);
		actionbar.setDefaultVerticalComponentAlignment(Alignment.BASELINE);
		actionbar.add(textField, button);
	}
}
