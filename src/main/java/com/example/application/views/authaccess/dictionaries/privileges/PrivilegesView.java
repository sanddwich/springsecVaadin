package com.example.application.views.authaccess.dictionaries.privileges;

import com.example.application.views.layouts.MainLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.security.PermitAll;

@PermitAll
@PageTitle("PrivilegesView")
@Route(value = "/privileges", layout = MainLayout.class)
public class PrivilegesView extends HorizontalLayout {

	public PrivilegesView() {
		allConfig();
	}

	public void allConfig() {

		thisConfig();
	}

	public void thisConfig() {
		setSizeFull();
		add(
		  new H1("PrivilegesView")
		);
	}

}

