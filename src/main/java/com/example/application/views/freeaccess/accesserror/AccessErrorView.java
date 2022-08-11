package com.example.application.views.freeaccess.accesserror;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@PageTitle("Access Error")
@Route("/auth/error_access")
@AnonymousAllowed
public class AccessErrorView extends VerticalLayout {
	public AccessErrorView() {
		add(new H1("Access Error"));
	}
}
