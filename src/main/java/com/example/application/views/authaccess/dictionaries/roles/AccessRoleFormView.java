package com.example.application.views.authaccess.dictionaries.roles;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

public class AccessRoleFormView extends VerticalLayout {
    public TextField name = createDefaultTextField("Имя роли");
    public TextField code = createDefaultTextField("Код роли");
    public TextField description = createDefaultTextField("Описание роли");
    HorizontalLayout actionButtonLayout = new HorizontalLayout();
    public Button addAccessRoleButton = new Button("Создать");
    public Button updateAccessRoleButton = new Button("Обновить");
    public Button deleteAccessRoleButton = new Button("Удалить");
    public Button managePrivilegesButton = new Button("Привилегии роли");
    HorizontalLayout header = new HorizontalLayout();
    public Icon closeIconButton;

    public AccessRoleFormView() {
        allConfig();
    }

    public void allConfig() {
        closeIconButtonConfig();
        headerConfig();
        addAccessRoleButtonConfig();
        updateAccessRoleButtonConfig();
        deleteAccessRoleButtonConfig();
        managePrivilegesButtonConfig();
        actionButtonLayoutConfig();
        thisConfig();
    }

    public void thisConfig() {
        addClassName("AccessRoleFormView");
        addClassNames("pl-0", "pt-0");
        setSizeFull();
        add(
                header,
                name,
                code,
                description,
                actionButtonLayout,
                managePrivilegesButton
        );
    }

    public void actionButtonLayoutConfig() {
        actionButtonLayout.setWidthFull();
        actionButtonLayout.setAlignItems(Alignment.CENTER);
        actionButtonLayout.setJustifyContentMode(JustifyContentMode.BETWEEN);
        actionButtonLayout.add(
                addAccessRoleButton,
                updateAccessRoleButton,
                deleteAccessRoleButton
        );
    }

    public void addAccessRoleButtonConfig() {
        addAccessRoleButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    }

    public void updateAccessRoleButtonConfig() {
    }

    public void deleteAccessRoleButtonConfig() {
        deleteAccessRoleButton.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_PRIMARY);
    }

    public void managePrivilegesButtonConfig() {
        managePrivilegesButton.setWidthFull();
        managePrivilegesButton.addClassName("pt-0");
        managePrivilegesButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    }

    public void headerConfig() {
        header.setSpacing(false);
        header.setPadding(false);
        header.addClassName("pt-m");
        header.setWidthFull();
        header.setJustifyContentMode(JustifyContentMode.END);
        header.setAlignItems(Alignment.CENTER);
        header.add(
                closeIconButton
        );
    }

    public void closeIconButtonConfig() {
        closeIconButton = new Icon(VaadinIcon.CLOSE_CIRCLE);
        closeIconButton.addClassName("closeButtonConfig");
        closeIconButton.setColor("hsla(21hsla(214, 50%, 22%, 0.26)");
    }

    public TextField createDefaultTextField(String textFieldName) {
        TextField textField = new TextField(textFieldName);
        textField.setWidthFull();
        textField.addClassName("py-0");
        return textField;
    }
}
