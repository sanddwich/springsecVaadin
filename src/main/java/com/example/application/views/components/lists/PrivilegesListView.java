package com.example.application.views.components.lists;

import com.example.application.entities.AccessRole;
import com.example.application.entities.Privilege;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;

public class PrivilegesListView extends VerticalLayout {
    public Grid<Privilege> privilegeGrid = privilegeGridCreate();
    public Button addPrivilegesButton = addPrivilegesButtonCreate();
    public Button deletePrivilegesButton = deletePrivilegesButtonCreate();
    public Icon closeIconButton = closeIconButtonCreate();
    HorizontalLayout header = headerCreate();
    HorizontalLayout actionBar = actionBarCreate();
    public TextField filterPrivileges = new TextField("Фильтр");

    public PrivilegesListView() {
        thisConfig();
    }

    public void thisConfig() {
        addClassName("PrivilegesListView");
        setSizeFull();
        filterPrivilegesConfig();
        add(header, filterPrivileges, privilegeGrid, actionBar);
    }

    public HorizontalLayout actionBarCreate() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.addClassName("actionBar");
        horizontalLayout.setWidthFull();
        horizontalLayout.setJustifyContentMode(JustifyContentMode.END);
        horizontalLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        horizontalLayout.add(addPrivilegesButton, deletePrivilegesButton);
        return horizontalLayout;
    }

    public void filterPrivilegesConfig() {
        filterPrivileges.addClassName("py-0");
        filterPrivileges.setLabel("Фильтр");
        filterPrivileges.setPlaceholder("Введите текст...");
        filterPrivileges.setClearButtonVisible(true);
        filterPrivileges.setValueChangeMode(ValueChangeMode.LAZY);
    }

    public HorizontalLayout headerCreate() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.addClassName("header");
        horizontalLayout.setWidthFull();
        horizontalLayout.setJustifyContentMode(JustifyContentMode.END);
        horizontalLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        horizontalLayout.add(closeIconButton);
        return horizontalLayout;
    }

    public Icon closeIconButtonCreate() {
        Icon icon = new Icon(VaadinIcon.CLOSE_CIRCLE);
        icon.addClassName("closeIconButton");
        icon.setColor("hsla(21hsla(214, 50%, 22%, 0.26)");
        return icon;
    }

    public Button addPrivilegesButtonCreate() {
        Button button = new Button("Добавить");
        button.addClassNames("vaadinButton", "addPrivilegesButton");
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        return button;
    }

    public Button deletePrivilegesButtonCreate() {
        Button button = new Button("Удалить");
        button.addClassNames("vaadinButton", "deletePrivilegesButton");
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        return button;
    }

    public Grid<Privilege> privilegeGridCreate() {
        Grid<Privilege> grid = new Grid<>(Privilege.class);
        grid.addClassName("privilegeGrid");
        grid.setSizeFull();
        grid.removeAllColumns();
        grid.addColumn(privilege -> privilege.getName()).setHeader("Имя привилегии");
        grid.addColumn(privilege -> privilege.getCode()).setHeader("Код привилегии");
        grid.addColumn(privilege -> privilege.getDescription()).setHeader("Описание привилегии");
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.getColumns().forEach(privilegeColumn -> privilegeColumn.setAutoWidth(true).setSortable(true));
        grid.setSelectionMode(Grid.SelectionMode.MULTI);

        grid.addItemClickListener(event -> {
            Privilege privilegeItem = event.getItem();
            if (grid.getSelectedItems().contains(privilegeItem)) {
                grid.deselect(privilegeItem);
            } else {
                grid.select(privilegeItem);
            }
        });

        return grid;
    }
}
