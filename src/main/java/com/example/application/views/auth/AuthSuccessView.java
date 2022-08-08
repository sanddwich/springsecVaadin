package com.example.application.views.auth;

import com.example.application.security.SecurityService;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.security.PermitAll;

@PageTitle("Успешная авторизация")
@Route(value = "/auth/success")
public class AuthSuccessView extends VerticalLayout {
    private Button button;

    public AuthSuccessView() {
        this.configButton();
        add(new H3("Успешная авторизация"), this.button);

    }

    public void configButton() {
        this.button = new Button("Выход");
        this.button.addClickListener(this::logoutButtonHandler);
    }

    private void logoutButtonHandler(ClickEvent<Button> buttonClickEvent) {
        SecurityService securityService = new SecurityService();
        securityService.logout();
    }

}
