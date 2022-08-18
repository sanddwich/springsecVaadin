package com.example.application.views.authaccess.dictionaries.roles;

import com.example.application.views.layouts.MainLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.security.PermitAll;

@PermitAll
@PageTitle("AccessRolesView")
@Route(value = "/roles", layout = MainLayout.class)
public class AccessRolesView extends HorizontalLayout {

	public AccessRolesView() {
		allConfig();
	}

	public void allConfig() {

		thisConfig();
	}

	public void thisConfig() {
		setSizeFull();
		add(
		  new H1("AccessRolesView")
		);
	}

}
