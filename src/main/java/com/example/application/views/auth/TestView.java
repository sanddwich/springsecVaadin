package com.example.application.views.auth;

import com.example.application.security.Guarded;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.security.PermitAll;

@PageTitle("TestView")
@Route(value = "/auth/test")
@PermitAll
@Guarded({"ADMIN"})
public class TestView extends VerticalLayout {

    public TestView() {
        add(new H3("TestView"));
    }
}
