package com.example.application.security;

import com.example.application.views.auth.LoginView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.NotFoundException;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import org.springframework.stereotype.Component;

@Component
public class ConfigureUIServiceInitListener implements VaadinServiceInitListener {
	@Override
	public void serviceInit(ServiceInitEvent serviceInitEvent) {
		serviceInitEvent.getSource().addUIInitListener(uiEvent -> {
			final UI ui = uiEvent.getUI();
			ui.addBeforeEnterListener(this::beforeEnter);
		});
	}

	private void beforeEnter(BeforeEnterEvent event) {
		if (!SecurityService.isAccessGranted(event.getNavigationTarget())) { // (1)
			if (SecurityService.isUserLoggedIn()) { // (2)
				event.rerouteToError(NotFoundException.class); // (3)
			} else {
				event.rerouteTo(LoginView.class); // (4)
			}
		}
	}
}
