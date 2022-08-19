package com.example.application.views.components.dialog;

import com.example.application.entities.AccessRole;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import java.util.List;
import java.util.stream.Collectors;

public class AccessRolesDialogView extends Dialog {
	public Button addAccessRolesButton = new Button("Добавить");
	Button closeDialogButton = new Button("Отмена");
	Icon closeDialogIcon = new Icon(VaadinIcon.CLOSE_CIRCLE);
	public Grid<AccessRole> accessRoleGrid = new Grid<>(AccessRole.class);
	List<AccessRole> additionalAccessRoles;

	public AccessRolesDialogView() {
		allConfig();
	}

	public void allConfig() {
		addAccessRolesButtonConfig();
		closeDialogButtonConfig();
		closeDialogIconConfig();
		accessRoleGridConfig();
		thisConfig();
	}

	public void thisConfig() {
		addClassName("AccessRolesDialogView");
		setSizeFull();
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setWidthFull();
		horizontalLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
		horizontalLayout.add(closeDialogIcon);

		HorizontalLayout actionButtonLayout = new HorizontalLayout();
		actionButtonLayout.setWidthFull();
		actionButtonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
		actionButtonLayout.add(addAccessRolesButton);

		getHeader().add(horizontalLayout);
		getFooter().add(closeDialogButton);
		add(
		  accessRoleGrid,
		  actionButtonLayout
		);
	}

	public void addAccessRolesButtonConfig() {
		addAccessRolesButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		addAccessRolesButton.setEnabled(false);
	}

	public void closeDialogButtonConfig() {
		closeDialogButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		closeDialogButton.addClickListener(buttonClickEvent -> close());
	}

	public void closeDialogIconConfig() {
		closeDialogIcon.setColor("hsla(21hsla(214, 50%, 22%, 0.26)");
		closeDialogIcon.addClickListener(iconClickEvent -> close());
	}

	public void accessRoleGridConfig() {
		accessRoleGrid.addClassName("accessRoleGrid");
		accessRoleGrid.setSizeFull();
		accessRoleGrid.removeAllColumns();
		accessRoleGrid.addColumn(AccessRole::getName).setHeader("Имя");
		accessRoleGrid.addColumn(AccessRole::getCode).setHeader("Код");
		accessRoleGrid.addColumn(AccessRole::getDescription).setHeader("Расшифровка");
		accessRoleGrid.setSelectionMode(Grid.SelectionMode.MULTI);
		accessRoleGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
		accessRoleGrid.getColumns().forEach(accessRoleColumn -> {
			accessRoleColumn.setAutoWidth(true).setSortable(true);
		});

		accessRoleGrid.addItemClickListener(event -> {
			AccessRole accessRoleItem = event.getItem();
			if (accessRoleGrid.getSelectedItems().contains(accessRoleItem)) {
				accessRoleGrid.deselect(accessRoleItem);
			} else {
				accessRoleGrid.select(accessRoleItem);
			}
		});

		accessRoleGrid.addSelectionListener(event -> {
			additionalAccessRoles = event.getAllSelectedItems().stream().collect(Collectors.toList());

			addAccessRolesButton.setEnabled(!additionalAccessRoles.isEmpty());
		});

	}
}
