package com.example.application.views.auth;

import com.example.application.security.SecurityService;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.annotation.security.PermitAll;

@PageTitle("Успешная авторизация")
@Route(value = "/auth/success")
@PermitAll
public class AuthSuccessView extends VerticalLayout {
    private Button button;
    private Button additionalButton;

    public AuthSuccessView() {
        this.configAll();
        add(new H3("Успешная авторизация"), this.button, this.additionalButton);

    }

    public void configAll() {
        this.configButton();
        this.configAdditionalButton();
    }

    public void configButton() {
        this.button = new Button("Выход");
        this.button.addClickListener(this::logoutButtonHandler);
    }

    public void configAdditionalButton() {
        this.additionalButton = new Button("additionalButton");
        this.additionalButton.addClickListener(this::additionalButtonHandler);
    }

    private void additionalButtonHandler(ClickEvent<Button> buttonClickEvent) {
//        // lookup needed role in user roles
//        List<String> allowedPrivileges = Arrays.asList(guarded.value());
//        Authentication userAuthentication = SecurityContextHolder.getContext().getAuthentication();
//
//        return userAuthentication.getAuthorities().stream() // (2)
//          .map(GrantedAuthority::getAuthority)
//          .anyMatch(allowedPrivileges::contains);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication.toString());

        System.out.println(authentication.getPrincipal().toString());
    }

    private void logoutButtonHandler(ClickEvent<Button> buttonClickEvent) {
        SecurityService securityService = new SecurityService();
        securityService.logout();
    }

}
