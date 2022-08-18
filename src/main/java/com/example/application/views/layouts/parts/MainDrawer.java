package com.example.application.views.layouts.parts;

import com.example.application.views.authaccess.dictionaries.users.UserListView;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MainDrawer extends VerticalLayout {
	AppRouterLinks appRouterLinks;

	public MainDrawer(AppRouterLinks appRouterLinks) {
		this.appRouterLinks = appRouterLinks;
		allConfig();
	}

	public void allConfig() {
		thisConfig();
	}

	public void thisConfig() {
		addClassName("mainDrawer");
		appRouterLinks.getRouterLinkList().forEach(routerLink -> add(routerLink));
	}
}
