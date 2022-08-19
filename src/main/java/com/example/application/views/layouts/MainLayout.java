package com.example.application.views.layouts;

import com.example.application.views.layouts.parts.MainDrawer;
import com.example.application.views.layouts.parts.MainHeader;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;

public class MainLayout extends AppLayout implements BeforeEnterObserver {
	private final MainHeader mainHeader;
	private final MainDrawer mainDrawer;

	public MainLayout(
	  MainHeader mainHeader,
	  MainDrawer mainDrawer
	) {
		this.mainHeader = mainHeader;
		this.mainDrawer = mainDrawer;
		allConfig();
	}

	public void allConfig() {
		mainHeaderConfig();
		mainDrawerConfig();
		thisConfig();
	}

	public void thisConfig() {
		setDrawerOpened(false);
		addToNavbar(mainHeader);
		addToDrawer(mainDrawer);
	}

	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		if (isDrawerOpened()) setDrawerOpened(false);
	}

	public void mainHeaderConfig() {
	}

	public void mainDrawerConfig() {
	}
}
