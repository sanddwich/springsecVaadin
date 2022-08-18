package com.example.application.views.authaccess;

import com.example.application.security.SecurityService;
import com.example.application.views.layouts.MainLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.security.PermitAll;

@PermitAll
@PageTitle("LogoutView")
@Route(value = "auth/logout", layout = MainLayout.class)
public class LogoutView extends HorizontalLayout {
	SecurityService securityService;

	public LogoutView(SecurityService securityService) {
		this.securityService = securityService;

		securityService.logout();
	}
}
