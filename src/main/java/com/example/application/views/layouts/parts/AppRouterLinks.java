package com.example.application.views.layouts.parts;

import com.example.application.views.authaccess.LogoutView;
import com.example.application.views.authaccess.dictionaries.privileges.PrivilegesView;
import com.example.application.views.authaccess.dictionaries.roles.AccessRolesView;
import com.example.application.views.authaccess.dictionaries.users.UserListView;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AppRouterLinks {
	private List<RouterLink> routerLinkList;

	@Autowired
	public AppRouterLinks() {
		routerLinkListConfig();
	}

	public List<RouterLink> getRouterLinkList() {
		return routerLinkList;
	}

	private void routerLinkListConfig() {
		routerLinkList = Stream.of(
		  new RouterLink("Пользователи", UserListView.class),
		  new RouterLink("Роли", AccessRolesView.class),
		  new RouterLink("Привилегии", PrivilegesView.class),
		  new RouterLink( "Выход", LogoutView.class)
		).collect(Collectors.toList());

		routerLinkList.forEach(routerLink -> {
			routerLink.setHighlightCondition(HighlightConditions.sameLocation());
			routerLink.setClassName("routerLink");
		});
	}
}
