package com.example.application.views.components.dialog;

import com.example.application.entities.AccessRole;
import com.example.application.entities.Privilege;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;

import java.util.List;
import java.util.stream.Collectors;

public class PrivilegesDialogView extends Dialog {
	public Button addPrivilegesButton = new Button("Добавить");
	Button closeDialogButton = new Button("Отмена");
	Icon closeDialogIcon = new Icon(VaadinIcon.CLOSE_CIRCLE);
	public Grid<Privilege> privilegesGrid = new Grid<>(Privilege.class);
	List<Privilege> additionalPrivileges;
	public TextField filterPrivileges = new TextField("Фильтр");

	public PrivilegesDialogView() {
		allConfig();
	}

	public void allConfig() {
		addPrivilegesButtonConfig();
		closeDialogButtonConfig();
		closeDialogIconConfig();
		privilegesGridConfig();
		filterPrivilegeConfig();
		thisConfig();
	}

	public void thisConfig() {
		addClassName("PrivilegesDialogView");
		setSizeFull();
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setWidthFull();
		horizontalLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
		horizontalLayout.add(closeDialogIcon);

		HorizontalLayout actionButtonLayout = new HorizontalLayout();
		actionButtonLayout.setWidthFull();
		actionButtonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
		actionButtonLayout.add(addPrivilegesButton);

		getHeader().add(horizontalLayout);
		getFooter().add(closeDialogButton);
		add(
		  filterPrivileges,
		  privilegesGrid,
		  actionButtonLayout
		);
	}

	public void filterPrivilegeConfig() {
		filterPrivileges.setPlaceholder("Введите текст...");
		filterPrivileges.addClassName("pt-0");
		filterPrivileges.setClearButtonVisible(true);
		filterPrivileges.setValueChangeMode(ValueChangeMode.LAZY);
	}

	public void addPrivilegesButtonConfig() {
		addPrivilegesButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		addPrivilegesButton.setEnabled(false);
	}

	public void closeDialogButtonConfig() {
		closeDialogButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		closeDialogButton.addClickListener(buttonClickEvent -> close());
	}

	public void closeDialogIconConfig() {
		closeDialogIcon.setColor("hsla(21hsla(214, 50%, 22%, 0.26)");
		closeDialogIcon.addClickListener(iconClickEvent -> close());
	}

	public void privilegesGridConfig() {
		privilegesGrid.addClassName("privilegesGrid");
		privilegesGrid.setSizeFull();
		privilegesGrid.removeAllColumns();
		privilegesGrid.addColumn(Privilege::getName).setHeader("Имя");
		privilegesGrid.addColumn(Privilege::getCode).setHeader("Код");
		privilegesGrid.addColumn(Privilege::getDescription).setHeader("Расшифровка");
		privilegesGrid.setSelectionMode(Grid.SelectionMode.MULTI);
		privilegesGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
		privilegesGrid.getColumns().forEach(accessRoleColumn -> {
			accessRoleColumn.setAutoWidth(true).setSortable(true);
		});

		privilegesGrid.addItemClickListener(event -> {
			Privilege privilegeItem = event.getItem();
			if (privilegesGrid.getSelectedItems().contains(privilegeItem)) {
				privilegesGrid.deselect(privilegeItem);
			} else {
				privilegesGrid.select(privilegeItem);
			}
		});

		privilegesGrid.addSelectionListener(event -> {
			additionalPrivileges = event.getAllSelectedItems().stream().collect(Collectors.toList());

			addPrivilegesButton.setEnabled(!additionalPrivileges.isEmpty());
		});

	}
}
