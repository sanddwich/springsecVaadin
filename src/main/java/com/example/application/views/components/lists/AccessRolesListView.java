package com.example.application.views.components.lists;

import com.example.application.data.Status;
import com.example.application.entities.AccessRole;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class AccessRolesListView extends VerticalLayout {
    public Grid<AccessRole> accessRoleGrid = new Grid<>(AccessRole.class);

    public AccessRolesListView() {
        allConfig();
    }

    public void allConfig() {
        accessRoleGridConfig();
        thisConfig();
    }

    public void thisConfig() {
        addClassName("AccessRolesListView");
        setSizeFull();
        add(accessRoleGrid);
    }

    public void accessRoleGridConfig() {
        accessRoleGrid.addClassName("accessRoleGrid");
        accessRoleGrid.setSizeFull();
//		userGrid.setColumns("username", "email", "active");
        accessRoleGrid.removeAllColumns();
        accessRoleGrid.addColumn(accessRole -> accessRole.getName()).setHeader("Имя роли");
        accessRoleGrid.addColumn(accessRole -> accessRole.getCode()).setHeader("Код роли");
        accessRoleGrid.addColumn(accessRole -> accessRole.getDescription()).setHeader("Описание роли");
        accessRoleGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        accessRoleGrid.getColumns().forEach(accessRoleColumn -> accessRoleColumn.setAutoWidth(true).setSortable(true));

    }
}
