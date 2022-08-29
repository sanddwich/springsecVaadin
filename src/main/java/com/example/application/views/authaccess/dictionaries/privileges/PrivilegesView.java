package com.example.application.views.authaccess.dictionaries.privileges;

import com.example.application.entities.AccessRole;
import com.example.application.entities.Privilege;
import com.example.application.services.PrivilegeService;
import com.example.application.views.components.lists.PrivilegesListView;
import com.example.application.views.layouts.MainLayout;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.ItemClickEvent;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.security.PermitAll;
import java.util.List;

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
		System.out.println("filterTextFieldChangeHandler");
	}

	public PrivilegesFormView createPrivilegesFormView() {
		PrivilegesFormView privilegesListFormItem = new PrivilegesFormView();
		privilegesListFormItem.setWidth("25em");
		privilegesListFormItem.setVisible(false);
		return privilegesListFormItem;
	}

	private void privilegeClickHandler(ItemClickEvent<Privilege> privilegeItemClickEvent) {
		privilegeEdit(privilegeItemClickEvent.getItem());
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

