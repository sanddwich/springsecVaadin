package com.example.application.views.authaccess.dictionaries.roles;

import com.example.application.entities.AccessRole;
import com.example.application.services.AccessRoleService;
import com.example.application.services.PrivilegeService;
import com.example.application.views.components.lists.AccessRolesListView;
import com.example.application.views.components.lists.PrivilegesListView;
import com.example.application.views.components.notification.ErrorNotification;
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
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.security.PermitAll;
import java.util.Collections;
import java.util.Locale;

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
    AccessRole editedAccessRole;
    Binder<AccessRole> accessRoleBinder = new BeanValidationBinder<>(AccessRole.class);
    PrivilegesListView privilegesListView = PrivilegesListViewCreate();

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
                privilegesListView,
                leftVerticalLayout,
                accessRoleFormView
        );
    }

    public PrivilegesListView PrivilegesListViewCreate() {
        PrivilegesListView item = new PrivilegesListView();
        item.setVisible(false);
        item.closeIconButton.addClickListener(iconClickEvent -> this.privilegesViewOpenClose());
        item.addPrivilegesButton.addClickListener(this::addPrivilegesButtonClickHandler);
        item.deletePrivilegesButton.addClickListener(this::deletePrivilegesButtonClickHandler);
        item.deletePrivilegesButton.setEnabled(false);
        return item;
    }

    private void deletePrivilegesButtonClickHandler(ClickEvent<Button> buttonClickEvent) {
        System.out.println("deletePrivilegesButtonClickHandler");
    }

    public void addPrivilegesButtonClickHandler(ClickEvent<Button> buttonClickEvent) {
        System.out.println("addPrivilegesButtonClickHandler");
    }

    public void accessRoleFormViewConfig() {
        accessRoleFormView.setWidth("25em");
		accessRoleFormView.setVisible(false);
		accessRoleFormView.closeIconButton.addClickListener(this::closeIconButtonClickHandler);
		accessRoleFormView.addAccessRoleButton.addClickListener(this::addAccessRoleButtonClickHandler);
		accessRoleFormView.updateAccessRoleButton.addClickListener(this::updateAccessRoleButtonClickHandler);
		accessRoleFormView.deleteAccessRoleButton.addClickListener(this::deleteAccessRoleButtonClickHandler);
		accessRoleFormView.managePrivilegesButton.addClickListener(this::managePrivilegesButtonClickHandler);
    }

    private void managePrivilegesButtonClickHandler(ClickEvent<Button> buttonClickEvent) {
        privilegesViewOpenClose();
    }

    public void privilegesViewOpenClose() {
        leftVerticalLayout.setVisible(!leftVerticalLayout.isVisible());
        privilegesListView.setVisible(!privilegesListView.isVisible());
        accessRoleFormView.addAccessRoleButton.setEnabled(!accessRoleFormView.addAccessRoleButton.isEnabled());
        accessRoleFormView.updateAccessRoleButton.setEnabled(!accessRoleFormView.updateAccessRoleButton.isEnabled());
        accessRoleFormView.deleteAccessRoleButton.setEnabled(!accessRoleFormView.deleteAccessRoleButton.isEnabled());
        accessRoleFormView.name.setEnabled(!accessRoleFormView.name.isEnabled());
        accessRoleFormView.code.setEnabled(!accessRoleFormView.code.isEnabled());
        accessRoleFormView.description.setEnabled(!accessRoleFormView.description.isEnabled());
        accessRoleFormView.closeIconButton.setVisible(!accessRoleFormView.closeIconButton.isVisible());
    }

    public void deleteAccessRoleButtonClickHandler(ClickEvent<Button> buttonClickEvent) {
        try {
            if (accessRoleService.delete(editedAccessRole)) {
                accessRolesListUpdate();
                accessRoleEdit(null);
                ErrorNotification.showNotification("Роль \"" + editedAccessRole.getName() + "\" удалена!", false);
            } else {
                throw new Exception("Ошибка удаления роли!");
            }
        } catch(Exception e) {
            ErrorNotification.showNotification("Ошибка удаления роли: " + e.getMessage(), true);
        }
    }

    public void updateAccessRoleButtonClickHandler(ClickEvent<Button> buttonClickEvent) {
        try {
            accessRoleBinder.writeBean(editedAccessRole);
            if (editedAccessRole.getId() == null)
                throw new Exception("Роль не создана!\n Невозможно обновление");

            if (accessRoleIsExistByNameOrCodeWithOtherID(editedAccessRole))
                throw new Exception("Существует другая Роль\n с таким Именем или Кодом");

            if (nameOrCodeHasForbiddenChars(editedAccessRole))
                throw new Exception("В Имени или Коде роли недопустимо\n использование пробелов");

            if (accessRoleService.update(editedAccessRole).getId() != null) {
                ErrorNotification.showNotification("Роль обновлена!", false);
                accessRolesListUpdate();
                accessRoleEdit(null);
            } else {
                throw new Exception("Неизвестная ошибка обновления");
            }

        } catch(Exception e) {
            ErrorNotification.showNotification("Ошибка обновления роли: " + e.getMessage(), true);
        }
    }

    public boolean accessRoleIsExistByNameOrCodeWithOtherID(AccessRole inputAccessRole) {
        boolean result = false;

        if (
          accessRoleService.findByName(inputAccessRole.getName()).isEmpty()
          || accessRoleService.findByCode(inputAccessRole.getCode()).isEmpty()
        ) return false;

        AccessRole findAccessRoleByName = accessRoleService.findByName(inputAccessRole.getName()).stream().findFirst().get();
        AccessRole findAccessRoleByCode = accessRoleService.findByCode(inputAccessRole.getCode()).stream().findFirst().get();

        if (
          (findAccessRoleByName != null && !findAccessRoleByName.getId().equals(inputAccessRole.getId()))
          || (findAccessRoleByCode != null && !findAccessRoleByCode.getId().equals(inputAccessRole.getId()))
        ) result = true;

        return result;
    }

    public void addAccessRoleButtonClickHandler(ClickEvent<Button> buttonClickEvent) {
        try {
            accessRoleBinder.writeBean(editedAccessRole);
            if (accessRoleService.findAccessRoleByNameOrCode(editedAccessRole))
                throw new Exception("Создаваемая роль уже существует\n с таким Именем или Кодом");

            if (nameOrCodeHasForbiddenChars(editedAccessRole))
                throw new Exception("В Имени или Коде роли недопустимо\n использование пробелов");

            AccessRole addedAccessRole = new AccessRole(
              editedAccessRole.getName().toUpperCase(Locale.ROOT),
              editedAccessRole.getCode().toUpperCase(Locale.ROOT),
              editedAccessRole.getDescription().toUpperCase(Locale.ROOT),
              Collections.emptyList()
            );

            if (accessRoleService.save(addedAccessRole).getId() != null) {
                ErrorNotification.showNotification("Роль \"" + addedAccessRole.getName() + "\" создана!", false);
                accessRolesListUpdate();
                accessRoleEdit(null);
            } else {
                throw new Exception("Неизвестная ошибка обновления");
            }


        } catch(Exception e) {
            ErrorNotification.showNotification("Ошибка создания роли: " + e.getMessage(), true);
        }
    }

    public boolean nameOrCodeHasForbiddenChars(AccessRole inputAccessRole) {
        if (
          inputAccessRole.getName().replaceAll("\\s+","").length() < inputAccessRole.getName().length()
          || inputAccessRole.getCode().replaceAll("\\s+","").length() < inputAccessRole.getCode().length()
        ) return true;

        return false;
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
            editedAccessRole = accessRole;
            accessRoleBinder.readBean(editedAccessRole);
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
