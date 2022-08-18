package com.example.application.views.layouts.parts;

import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MainHeader extends HorizontalLayout {
	DrawerToggle drawerToggle;
	H1 logo;

	public MainHeader() {
		allConfig();
	}

	public void allConfig() {
		logoConfig();
		drawerToggleConfig();
		thisConfig();
	}

	public void thisConfig() {
		addClassName("MainHeader");
		addClassNames("py-0", "px-m");
		setAlignItems(Alignment.START);
		setDefaultVerticalComponentAlignment(Alignment.CENTER);
		setWidthFull();
		add(
		  drawerToggle,
		  logo
		);
	}

	public void drawerToggleConfig() {
		drawerToggle = new DrawerToggle();
	}

	public void logoConfig() {
		logo = new H1("Admin Panel");
		logo.addClassNames("text-l", "m-m");
	}
}
