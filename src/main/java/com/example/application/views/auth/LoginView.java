package com.example.application.views.auth;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Страница авторизации")
@Route(value = "/auth/login")
public class LoginView extends VerticalLayout implements BeforeEnterObserver {
    private LoginI18n loginI18n = LoginI18n.createDefault();
    private LoginI18n.Form loginI18nForm = loginI18n.getForm();
    LoginForm loginForm = new LoginForm();

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        // inform the user about an authentication error
        if(
                beforeEnterEvent.getLocation()
                        .getQueryParameters()
                        .getParameters()
                        .containsKey("error")
        ) {
            loginForm.setError(true);
        }
    }

    public LoginView() {
        allConfig();

        add(new H3("Административная панель"), loginForm);
    }

    public void allConfig() {
        configLoginView();
        configLoginForm();
    }

    private void configLoginView() {
        addClassName("login-view");
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);
    }

    public void configLoginForm() {
        this.loginI18nForm.setTitle("Авторизация");
        this.loginI18nForm.setUsername("Пользователь");
        this.loginI18nForm.setPassword("Пароль");
        this.loginI18nForm.setSubmit("Вход");
        this.loginI18nForm.setForgotPassword("Запамятовал пароль");
        this.loginI18n.setForm(this.loginI18nForm);

        LoginI18n.ErrorMessage errorMessage = this.loginI18n.getErrorMessage();
        errorMessage.setTitle("Ошибка авторизации");
        errorMessage.setMessage("Проверьте корректность заполненных полей");
        this.loginI18n.setErrorMessage(errorMessage);

        this.loginForm.setI18n(this.loginI18n);

        this.loginForm.setAction("/auth/login");
    }
}
