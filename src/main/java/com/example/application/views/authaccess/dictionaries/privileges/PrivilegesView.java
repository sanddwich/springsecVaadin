package com.example.application.views.authaccess.dictionaries.privileges;

import com.example.application.entities.AccessRole;
import com.example.application.entities.Privilege;
import com.example.application.services.PrivilegeService;
import com.example.application.views.components.lists.PrivilegesListView;
import com.example.application.views.components.notification.ErrorNotification;
import com.example.application.views.layouts.MainLayout;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.ItemClickEvent;
import com.vaadin.flow.component.html.H1;
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
import java.util.List;
import java.util.Locale;

@PermitAll
@PageTitle("PrivilegesView")
@Route(value = "/privileges", layout = MainLayout.class)
public class PrivilegesView extends HorizontalLayout {
	private final PrivilegeService privilegeService;
	Grid<Privilege> privilegesList = new Grid<>(Privilege.class);
	public PrivilegesFormView privilegesFormView = createPrivilegesFormView();
	Binder<Privilege> privilegeBinder = new BeanValidationBinder<>(Privilege.class);

	VerticalLayout leftVerticalLayout = new VerticalLayout();
	TextField filterTextField = new TextField("Фильтр");
	HorizontalLayout filterLine = new HorizontalLayout();
	Button addPrivilegeButton = new Button("Добавить");
	Privilege editedPrivilege;

	public PrivilegesView(PrivilegeService privilegeService) {
		this.privilegeService = privilegeService;
		privilegeBinder.bindInstanceFields(privilegesFormView);
		allConfig();

		privilegeListUpdate();
	}

	public void allConfig() {
		addPrivilegeButtonConfig();
		filterLineConfig();
		privilegesListConfig();
		leftVerticalLayoutConfig();
		filterTextFieldConfig();
		thisConfig();
	}

	public void thisConfig() {
		setSizeFull();
		addClassName("PrivilegesView");
		add(
		  leftVerticalLayout,
		  privilegesFormView
		);
	}

	public void addPrivilegeButtonConfig() {
		addPrivilegeButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		addPrivilegeButton.setEnabled(false);
		addPrivilegeButton.addClickListener(this::addPrivilegeClickHandler);
	}

	private void addPrivilegeClickHandler(ClickEvent<Button> buttonClickEvent) {
		Privilege newPrivilege = new Privilege(
		  filterTextField.getValue(),
		  filterTextField.getValue(),
		  filterTextField.getValue()
		);

		privilegeEdit(newPrivilege);
	}

	private void privilegesListConfig() {
		privilegesList.addClassName("privilegesListGrid");
		privilegesList.setSizeFull();
		privilegesList.removeAllColumns();
		privilegesList.addColumn(privilege -> privilege.getName()).setHeader("Имя привилегии");
		privilegesList.addColumn(privilege -> privilege.getCode()).setHeader("Код привилегии");
		privilegesList.addColumn(privilege -> privilege.getDescription()).setHeader("Описание привилегии");
		privilegesList.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
		privilegesList.getColumns().forEach(privilegeColumn -> privilegeColumn.setAutoWidth(true).setSortable(true));
		privilegesList.asSingleSelect().addValueChangeListener(event -> privilegeEdit(event.getValue()));
	}

	public void privilegeEdit(Privilege privilege) {
		if (privilege != null) {
			editedPrivilege = privilege;
			privilegeBinder.readBean(editedPrivilege);
			privilegesFormView.setVisible(true);
		} else {
			privilegesFormView.setVisible(false);
		}
	}

	public void filterTextFieldConfig() {
		filterTextField.addClassName("py-0");
		filterTextField.setLabel("Фильтр");
		filterTextField.setPlaceholder("Введите текст...");
		filterTextField.setClearButtonVisible(true);
		filterTextField.setValueChangeMode(ValueChangeMode.LAZY);
		filterTextField.addValueChangeListener(this::filterTextFieldChangeHandler);
	}

	public void filterTextFieldChangeHandler(AbstractField.ComponentValueChangeEvent<TextField, String> textFieldStringComponentValueChangeEvent) {
		privilegeListUpdate();
		addPrivilegeButton.setEnabled(!filterTextField.isEmpty());
	}

	public PrivilegesFormView createPrivilegesFormView() {
		PrivilegesFormView privilegesListFormItem = new PrivilegesFormView();
		privilegesListFormItem.setWidth("25em");
		privilegesListFormItem.setVisible(false);
		privilegesListFormItem.closeIconButton.addClickListener(event -> closeFormButtonClickHandler(event));
		privilegesListFormItem.addPrivilegeButton.addClickListener(this::addPrivilegeButtonClickListener);
		privilegesListFormItem.updatePrivilegeButton.addClickListener(this::updatePrivilegeButtonClickListener);
		privilegesListFormItem.deletePrivilegeButton.addClickListener(this::deletePrivilegeButtonClickListener);
		return privilegesListFormItem;
	}

	public void addPrivilegeButtonClickListener(ClickEvent<Button> buttonClickEvent) {
		trySave();
	}

	public void updatePrivilegeButtonClickListener(ClickEvent<Button> buttonClickEvent) {
		tryUpdate();
	}

	public void deletePrivilegeButtonClickListener(ClickEvent<Button> buttonClickEvent) {
		tryDelete();
	}

	private void tryDelete() {
		try {
			if (privilegeService.delete(editedPrivilege)) {
				afterPrivilegeAction();
				ErrorNotification.showNotification("Привилегия \"" + editedPrivilege.getName() + "\" удалена!", false);
			} else {
				throw new Exception("Ошибка удаления роли!");
			}
		} catch (Exception e) {
			ErrorNotification.showNotification("Ошибка удаления роли: " + e.getMessage(), true);
		}
	}

	private void trySave() {
		try {
			privilegeBinder.writeBean(editedPrivilege);
			if (nameOrCodeHasForbiddenChars(editedPrivilege))
				throw new Exception("Поля Код или Имя привилегии\n содержат пробелы");

			if (this.privilegeService.findPrivilegeByNameOrCode(editedPrivilege))
				throw new Exception("В БД уже есть привилегия с таким\n именем или кодом");

			Privilege createdPrivilege = new Privilege(
			  editedPrivilege.getName().toUpperCase(Locale.ROOT),
			  editedPrivilege.getCode().toUpperCase(Locale.ROOT),
			  editedPrivilege.getDescription()
			);

			this.privilegeService.save(createdPrivilege);
			afterPrivilegeAction();

			ErrorNotification.showNotification("Добавлена новая привилегия", false);

		} catch (Exception e) {
			ErrorNotification.showNotification("Ошибка сохранения: " + e.getMessage(), true);
		}
	}

	private void tryUpdate() {
		try {
			privilegeBinder.writeBean(editedPrivilege);
			if (editedPrivilege.getId() == null)
				throw new Exception("Привилегия не создана!\n Невозможно обновление");

			if (nameOrCodeHasForbiddenChars(editedPrivilege))
				throw new Exception("Поля Код или Имя привилегии\n содержат пробелы");

			if (privilegeIsExist(editedPrivilege))
				throw new Exception("В БД уже есть привилегия с таким\n именем или кодом");

			this.privilegeService.update(editedPrivilege);
			afterPrivilegeAction();

			ErrorNotification.showNotification("Привилегия обновлена", false);

		} catch (Exception e) {
			ErrorNotification.showNotification("Ошибка сохранения: " + e.getMessage(), true);
		}
	}

	private boolean privilegeIsExist(Privilege inputPrivilege) {
		Privilege privilegeByCode = !this.privilegeService.findByCode(inputPrivilege.getCode()).isEmpty()
		  ? this.privilegeService.findByCode(inputPrivilege.getCode()).stream().findFirst().get()
		  : null;
		Privilege privilegeByName = !this.privilegeService.findByName(inputPrivilege.getName()).isEmpty()
		  ? this.privilegeService.findByName(inputPrivilege.getName()).stream().findFirst().get()
		  : null;

		if (privilegeByCode != null && privilegeByCode.getId() != inputPrivilege.getId()) return true;
		if (privilegeByName != null && privilegeByName.getId() != inputPrivilege.getId()) return true;

		return false;
	}

	private void afterPrivilegeAction() {
		filterTextField.clear();
		privilegeEdit(null);
		privilegeListUpdate();
	}

	public boolean nameOrCodeHasForbiddenChars(Privilege inputPrivilege) {
		if (
		  inputPrivilege.getName().replaceAll("\\s+", "").length() < inputPrivilege.getName().length()
			|| inputPrivilege.getCode().replaceAll("\\s+", "").length() < inputPrivilege.getCode().length()
		) return true;

		return false;
	}

	private void closeFormButtonClickHandler(ClickEvent<Icon> event) {
		privilegeEdit(null);
		filterTextField.clear();
		privilegeListUpdate();
	}

	public void privilegeListUpdate() {
		if (filterTextField.getValue().isEmpty()) {
			privilegesList.setItems(
			  privilegeService.findAll()
			);
		} else {
			privilegesList.setItems(
			  privilegeService.search(filterTextField.getValue())
			);
		}
	}

	public void leftVerticalLayoutConfig() {
		leftVerticalLayout.setSizeFull();
		leftVerticalLayout.add(
		  filterLine,
		  privilegesList
		);
	}

	public void filterLineConfig() {
		filterLine.setWidthFull();
		filterLine.addClassName("py-0");
		filterLine.setJustifyContentMode(JustifyContentMode.START);
		filterLine.setDefaultVerticalComponentAlignment(Alignment.BASELINE);
		filterLine.add(
		  filterTextField,
		  addPrivilegeButton
		);
	}
}

