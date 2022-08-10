package com.example.application.views.auth;

import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("TestView")
@Route(value = "/auth/test")
public class TestView extends VerticalLayout {

    public TestView() {
        add(new H3("TestView"));
    }
}
