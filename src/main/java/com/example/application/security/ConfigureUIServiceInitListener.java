package com.example.application.security;

import com.example.application.views.authaccess.authresult.AuthSuccessView;
import com.example.application.views.freeaccess.accesserror.AccessErrorView;
import com.example.application.views.freeaccess.auth.LoginView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.internal.JavaScriptBootstrapUI;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.NotFoundException;
import com.vaadin.flow.router.RouteNotFoundError;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ConfigureUIServiceInitListener implements VaadinServiceInitListener {

	@Value("${params.SUCCESS_AUTH_URL}")
	private String SUCCESS_AUTH_URL;

	@Override
	public void serviceInit(ServiceInitEvent serviceInitEvent) {
		serviceInitEvent.getSource().addUIInitListener(uiEvent -> {
			final UI ui = uiEvent.getUI();
			ui.addBeforeEnterListener(this::beforeEnter);
		});
	}

	// With @Granted Auth
	private void beforeEnter(BeforeEnterEvent event) {
		if ( SecurityService.isUserLoggedIn()) { // (1)
			if (!SecurityService.isAccessGranted(event.getNavigationTarget())) {
				event.rerouteToError(NotFoundException.class); // (3)
//				event.rerouteTo(AccessErrorView.class);
			}

			if (event.getNavigationTarget().equals(LoginView.class))
				event.rerouteTo(AuthSuccessView.class);
		}
	}

	public void authDefaultReroute(BeforeEnterEvent event) {

	}
}
