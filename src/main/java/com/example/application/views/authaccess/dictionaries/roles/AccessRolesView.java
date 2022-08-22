package com.example.application.views.authaccess.dictionaries.roles;

import com.example.application.entities.AccessRole;
import com.example.application.services.AccessRoleService;
import com.example.application.services.PrivilegeService;
import com.example.application.views.components.lists.AccessRolesListView;
import com.example.application.views.layouts.MainLayout;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.security.PermitAll;
import java.util.Collections;

@PermitAll
@PageTitle("AccessRolesView")
@Route(value = "/roles", layout = MainLayout.class)
@CssImport("./styles/AccessRolesView.css")
public class AccessRolesView extends HorizontalLayout {
    AccessRoleFormView accessRoleFormView = new AccessRoleFormView();
    AccessRolesListView accessRolesListView = new AccessRolesListView();
    VerticalLayout leftVerticalLayout = new VerticalLayout();
    TextField filterTextField = new TextField("Поиск ролей");
    HorizontalLayout filterLine = new HorizontalLayout();
    Button addAccessRoleButton = new Button("Добавить");

    Binder<AccessRole> accessRoleBinder = new Binder<>(AccessRole.class);

    private final AccessRoleService accessRoleService;
    private final PrivilegeService privilegeService;

    public AccessRolesView(
            AccessRoleService accessRoleService,
            PrivilegeService privilegeService
    ) {
        this.accessRoleService = accessRoleService;
        this.privilegeService = privilegeService;
        accessRoleBinder.bindInstanceFields(accessRoleFormView);

        allConfig();
    }

    public void allConfig() {
        filterLineConfig();
        addAccessRoleButtonConfig();
        accessRoleFormViewConfig();
        accessRolesListViewConfig();
        leftVerticalLayoutConfig();
        filterTextFieldConfig();
        thisConfig();
    }

    public void thisConfig() {
        addClassName("AccessRolesView");
        setSpacing(false);
        setSizeFull();
        add(
                leftVerticalLayout,
                accessRoleFormView
        );
    }

    public void accessRoleFormViewConfig() {
        accessRoleFormView.setWidth("25em");
		accessRoleFormView.setVisible(false);
		accessRoleFormView.closeIconButton.addClickListener(this::closeIconButtonClickHandler);
    }

    public void closeIconButtonClickHandler(ClickEvent<Icon> iconClickEvent) {
        accessRoleFormView.setVisible(false);
    }

    public void accessRolesListViewConfig() {
        accessRolesListView.setPadding(false);
        accessRolesListView.setSpacing(false);
        accessRolesListView.accessRoleGrid.asSingleSelect().addValueChangeListener(event -> accessRoleEdit(event.getValue()));

        accessRolesListUpdate();
    }

    public void accessRoleEdit(AccessRole accessRole) {
        if (accessRole != null) {
            accessRoleBinder.readBean(accessRole);
            accessRoleFormView.managePrivilegesButton.setEnabled(accessRole.getId() != null);
            accessRoleFormView.setVisible(true);
        } else {
            accessRoleFormView.setVisible(false);
        }

    }

    public void accessRolesListUpdate() {
        if (filterTextField.getValue().isEmpty()) {
            accessRolesListView.accessRoleGrid.setItems(
                    accessRoleService.findAll()
            );
        } else {
            accessRolesListView.accessRoleGrid.setItems(
                    accessRoleService.search(filterTextField.getValue())
            );
        }
    }

    public void leftVerticalLayoutConfig() {
        leftVerticalLayout.setSizeFull();
        leftVerticalLayout.add(
                filterLine,
                accessRolesListView
        );
    }

    public void filterTextFieldConfig() {
        filterTextField.addClassName("py-0");
        filterTextField.setLabel("Поиск ролей");
        filterTextField.setPlaceholder("Введите текст...");
        filterTextField.setClearButtonVisible(true);
        filterTextField.setValueChangeMode(ValueChangeMode.LAZY);
        filterTextField.addValueChangeListener(this::filterTextFieldChangeHandler);
    }

    public void filterTextFieldChangeHandler(AbstractField.ComponentValueChangeEvent<TextField, String> textFieldStringComponentValueChangeEvent) {
        accessRoleFormView.setVisible(false);
        addAccessRoleButton.setEnabled(!filterTextField.isEmpty());

        accessRolesListUpdate();
    }

    public void addAccessRoleButtonConfig() {
        addAccessRoleButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addAccessRoleButton.addClickListener(this::redactAccessRoleButtonClickHandler);
        addAccessRoleButton.setEnabled(false);
    }

    private void redactAccessRoleButtonClickHandler(ClickEvent<Button> buttonClickEvent) {
        AccessRole newAccessRole = new AccessRole(
                filterTextField.getValue(),
                filterTextField.getValue(),
                filterTextField.getValue(),
                Collections.emptyList()
        );

        accessRoleEdit(newAccessRole);
    }

    public void filterLineConfig() {
        filterLine.setWidthFull();
//        filterLine.setSpacing(false);
        filterLine.addClassName("py-0");
        filterLine.setJustifyContentMode(JustifyContentMode.START);
        filterLine.setDefaultVerticalComponentAlignment(Alignment.BASELINE);
        filterLine.add(
                filterTextField,
                addAccessRoleButton
        );
    }
}
