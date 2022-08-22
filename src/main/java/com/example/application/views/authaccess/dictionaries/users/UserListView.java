package com.example.application.views.authaccess.dictionaries.users;

import com.example.application.data.Status;
import com.example.application.entities.AccessRole;
import com.example.application.entities.User;
import com.example.application.services.AccessRoleService;
import com.example.application.services.UserService;
import com.example.application.views.components.dialog.AccessRolesDialogView;
import com.example.application.views.components.notification.ErrorNotification;
import com.example.application.views.layouts.MainLayout;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.security.PermitAll;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@PermitAll
@PageTitle("Users dictionary")
@Route(value = "/users", layout = MainLayout.class)
@CssImport("./styles/UserListView.css")
public class UserListView extends HorizontalLayout {
	private VerticalLayout actionBarWithGrid = new VerticalLayout();
	private VerticalLayout accessRolesLayout = new VerticalLayout();
	private HorizontalLayout accessRolesLayoutTitle = new HorizontalLayout();
	private HorizontalLayout accessRolesLayoutActionButtons = new HorizontalLayout();
	private H4 accessRolesTitle = new H4();
	private Icon closeButton = new Icon(VaadinIcon.CLOSE_CIRCLE);
	HorizontalLayout closeButtonContainer = new HorizontalLayout();
	private Button addAccessRoles = new Button();
	private Button detachAccessRoles = new Button();
	private HorizontalLayout actionbar = new HorizontalLayout();
	//	private HorizontalLayout content = new HorizontalLayout();
	private TextField filterTextField = new TextField();
	private Button actionButton = new Button();
	private Grid<User> userGrid = new Grid<>(User.class);
	private Grid<AccessRole> accessRoleGrid = new Grid<>(AccessRole.class);
	private UserFormView userFormView;
	private User user;
	private final UserService userService;
	private final AccessRoleService accessRoleService;
	private final String editClassName = "editing";
	private AccessRolesDialogView accessRolesDialogView = new AccessRolesDialogView();
	private List<AccessRole> userAccessRoles;
	private TextField filterAccessRoles = new TextField("Поиск ролей");

	public UserListView(
	  UserService userService,
	  AccessRoleService accessRoleService,
	  int BCRYPT_STRENGTH
	) {
		this.userService = userService;
		this.accessRoleService = accessRoleService;
		this.userFormView = new UserFormView(userService, BCRYPT_STRENGTH);

		allConfig();
		closeUserForm();
	}

	public void closeUserForm() {
		userFormView.setVisible(false);
		userFormView.setUser(null);
		removeClassName(editClassName);
	}

	public void updateUserList() {
		List<User> userList = filterTextField.getValue().isEmpty()
		  ? userService.findAll()
		  : userService.search(filterTextField.getValue());

		actionButton.setEnabled(userList.isEmpty());
		userGrid.setItems(userList);
	}

	private void updateAccessRoleList() {
		if (this.user != null) {
			List<AccessRole> userRoles = this.user.getAccessRoles();
			if (filterAccessRoles.getValue().isEmpty()) {
				accessRoleGrid.setItems(userRoles);
			} else {
				accessRoleGrid.setItems(
				  userRoles.stream()
					.filter(this::isContainFilterSearchTerm)
					.collect(Collectors.toList())
				);
			}
		}
	}

	public boolean isContainFilterSearchTerm(AccessRole inputAccessRole) {
		boolean result = false;
		if (
		  inputAccessRole.getName().toLowerCase(Locale.ROOT).contains(filterAccessRoles.getValue().toLowerCase(Locale.ROOT))
			|| inputAccessRole.getCode().toLowerCase(Locale.ROOT).contains(filterAccessRoles.getValue().toLowerCase(Locale.ROOT))
			|| inputAccessRole.getDescription().toLowerCase(Locale.ROOT).contains(filterAccessRoles.getValue().toLowerCase(Locale.ROOT))
		) {
			result = true;
		}

		return result;
	}

	private List<AccessRole> getDetachedAccessRoles() {
		return accessRoleService.findAll()
		  .stream().filter(this::isConsistInUserRoles)
		  .collect(Collectors.toList());
	}

	public boolean isConsistInUserRoles(AccessRole inputAccessRole) {
		boolean result = false;
		for (int iterator = 0; iterator < this.user.getAccessRoles().size(); iterator++) {
			if (this.user.getAccessRoles().get(iterator).getCode().equals(inputAccessRole.getCode())) {
				result = true;
				break;
			}
		}

		return !result;
	}

	public void allConfig() {
		thisConfig();
		userGridConfig();
		actionbarConfig();
		actionButtonConfig();
		filterTextFieldConfig();
		userFormViewConfig();
		actionBarWithGridConfig();
		accessRoleGridConfig();
		accessRolesLayoutConfig();
		accessRolesLayoutTitleConfig();
		addAccessRolesConfig();
		detachAccessRolesConfig();
		accessRolesLayoutActionButtonsConfig();
		closeButtonConfig();
		closeButtonContainerConfig();
		accessRolesDialogViewConfig();
		filterAccessRolesConfig();
	}

	public void filterAccessRolesConfig() {
		filterAccessRoles.setPlaceholder("Введите текст...");
		filterAccessRoles.addClassName("pt-0");
		filterAccessRoles.setClearButtonVisible(true);
		filterAccessRoles.setValueChangeMode(ValueChangeMode.LAZY);
		filterAccessRoles.addValueChangeListener(event -> {
			this.updateAccessRoleList();
		});
	}

	public void closeButtonContainerConfig() {
		closeButtonContainer.addClassName("closeButtonContainer");
		closeButtonContainer.addClassName("pt-s");
		closeButtonContainer.setWidthFull();
		closeButtonContainer.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
		closeButtonContainer.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.START);
		closeButtonContainer.add(
		  closeButton
		);
	}

	public void closeButtonConfig() {
		closeButton = new Icon(VaadinIcon.CLOSE_CIRCLE);
		closeButton.addClassName("closeButtonConfig");
		closeButton.setColor("hsla(21hsla(214, 50%, 22%, 0.26)");
		closeButton.addClickListener(this::closeFormHandler);
	}

	public void closeFormHandler(ClickEvent<Icon> iconClickEvent) {
		manageRoleEventHandler();
	}

	public void actionBarWithGridConfig() {
		actionBarWithGrid.setClassName("actionBarWithGrid");
		actionBarWithGrid.setClassName("py-0");
		actionBarWithGrid.add(
		  actionbar,
		  userGrid
		);
	}

	public void accessRolesLayoutConfig() {
		accessRolesLayout.addClassName("accessRolesLayout");
		accessRolesLayout.addClassNames("pl-0", "pt-0");
		accessRolesLayout.setSpacing(false);
		accessRolesLayout.setSizeFull();
		accessRolesLayout.setVisible(false);
		accessRolesLayout.add(
		  closeButtonContainer,
		  accessRolesLayoutTitle,
		  filterAccessRoles,
		  accessRoleGrid,
		  accessRolesLayoutActionButtons
		);
	}

	public void accessRolesLayoutTitleConfig() {
		accessRolesLayoutTitle.addClassName("accessRolesLayoutTitleConfig");
		accessRolesTitle.setClassName("mt-s");
		accessRolesLayoutTitle.add(
		  accessRolesTitle
		);
	}

	public void accessRolesLayoutActionButtonsConfig() {
		accessRolesLayoutActionButtons.addClassName("accessRolesLayoutActionButtons");
		accessRolesLayoutActionButtons.setJustifyContentMode(JustifyContentMode.END);
		accessRolesLayoutActionButtons.setDefaultVerticalComponentAlignment(Alignment.CENTER);
		accessRolesLayoutActionButtons.setWidthFull();
		accessRolesLayoutActionButtons.add(
		  addAccessRoles,
		  detachAccessRoles
		);
	}

	public void detachAccessRolesConfig() {
		detachAccessRoles.setText("Удалить");
		detachAccessRoles.addThemeVariants(ButtonVariant.LUMO_ERROR);
		detachAccessRoles.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		detachAccessRoles.setEnabled(false);
		detachAccessRoles.addClickListener(this::detachAccessRolesClickHandler);
	}

	public void detachAccessRolesClickHandler(ClickEvent<Button> buttonClickEvent) {
		this.user.setAccessRoles(
		  this.user.getAccessRoles()
			.stream()
			.filter(this::isConsistInDeleteRoles)
			.collect(Collectors.toList())
		);

		updateAccessRolesWithDB();
	}

	public void updateAccessRolesWithDB() {
		try {
			if (this.userService.update(this.user) != null) {
				filterAccessRoles.clear();
				updateAccessRoleList();
				ErrorNotification.showNotification("Роли пользователя изменены", false);
			} else {
				throw new Exception("Ошибка userService");
			}
		} catch (Exception e) {
			ErrorNotification.showNotification("Ошибка обновления ролей пользователя: " + e.getMessage(), true);
		}
	}

	public boolean isConsistInDeleteRoles(AccessRole inputAccessRole) {
		boolean result = false;
		for (int iterator = 0; iterator < userAccessRoles.size(); iterator++) {
			if (userAccessRoles.get(iterator).getCode().equals(inputAccessRole.getCode())) {
				result = true;
				break;
			}
		}

		return !result;
	}

	public void addAccessRolesConfig() {
		addAccessRoles.setText("Добавить");
		addAccessRoles.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		addAccessRoles.addClickListener(this::addAccessRoleClickHandler);
	}

	private void accessRolesDialogViewConfig() {
		accessRolesDialogView.accessRoleGrid.addClassName("p-s");
		accessRolesDialogView.addAccessRolesButton.addClickListener(this::addAdditionalAccessRolesToUser);
		accessRolesDialogView.filterAccessRoles.addValueChangeListener(this::updateAccessRolesAtDialog);
	}

	public void updateAccessRolesAtDialog(AbstractField.ComponentValueChangeEvent<TextField, String> textFieldStringComponentValueChangeEvent) {
		if (!accessRolesDialogView.filterAccessRoles.getValue().isEmpty()) {
			accessRolesDialogView.accessRoleGrid.setItems(
			  getDetachedAccessRoles()
			    .stream()
			    .filter(this::isDetachedRolesConsist)
			    .collect(Collectors.toList())
			);
		} else {
			accessRolesDialogView.accessRoleGrid.setItems(getDetachedAccessRoles());
		}

	}

	public boolean isDetachedRolesConsist(AccessRole inputAccessRole) {
		boolean result = false;
		if (
		  inputAccessRole.getCode().toLowerCase(Locale.ROOT).contains(accessRolesDialogView.filterAccessRoles.getValue().toLowerCase(Locale.ROOT))
		  || inputAccessRole.getName().toLowerCase(Locale.ROOT).contains(accessRolesDialogView.filterAccessRoles.getValue().toLowerCase(Locale.ROOT))
		  || inputAccessRole.getDescription().toLowerCase(Locale.ROOT).contains(accessRolesDialogView.filterAccessRoles.getValue().toLowerCase(Locale.ROOT))
		) {
			result = true;
		}

		return result;
	}

	private void addAdditionalAccessRolesToUser(ClickEvent<Button> buttonClickEvent) {
		this.user.getAccessRoles().addAll(
		  accessRolesDialogView.accessRoleGrid
			.getSelectedItems()
			.stream()
			.collect(Collectors.toList())
		);

		updateAccessRolesWithDB();
		accessRolesDialogView.close();
	}

	public void addAccessRoleClickHandler(ClickEvent<Button> buttonClickEvent) {
		accessRolesDialogView.accessRoleGrid.setItems(getDetachedAccessRoles());
		accessRolesDialogView.open();
	}

	private void userFormViewConfig() {
		userFormView.createButtonEvent(event -> createButtonEvent());
		userFormView.updateButtonEvent(event -> createButtonEvent());
		userFormView.deleteButtonEvent(event -> createButtonEvent());
		userFormView.closeFormEvent(event -> closeButtonHandler());
		userFormView.manageRoleEvent(event -> manageRoleEventHandler());
		userFormView.setWidth("25em");
	}

	public void thisConfig() {
		addClassNames("dictionaryView", "py-0");
//		setFlexGrow(2, actionBarWithGrid);
//		setFlexGrow(1, userFormView);
		setSpacing(false);
		setPadding(true);
		setSizeFull();
		add(
		  actionBarWithGrid,
		  accessRolesLayout,
		  userFormView,
		  accessRolesDialogView
		);
	}

	public void actionbarConfig() {
		actionbar.setWidthFull();
		actionbar.setJustifyContentMode(JustifyContentMode.START);
		actionbar.setDefaultVerticalComponentAlignment(Alignment.BASELINE);
		actionbar.add(filterTextField, actionButton);
	}

	public void actionButtonConfig() {
		actionButton.setText("Добавить");
		actionButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		actionButton.addClickListener(this::buttonHandler);
	}

	public void userGridConfig() {
		userGrid.setClassName("userGrid");
		userGrid.setSizeFull();
//		userGrid.setColumns("username", "email", "active");
		userGrid.removeAllColumns();
		userGrid.addColumn(user -> user.getUsername()).setHeader("Имя пользователя");
		userGrid.addColumn(user -> user.getEmail()).setHeader("email");
		userGrid.addColumn(user -> Status.boolToEnum(user.isActive())).setHeader("Активность");
		userGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
		userGrid.getColumns().forEach(userColumn -> userColumn.setAutoWidth(true).setSortable(true));

		userGrid.asSingleSelect().addValueChangeListener(event -> userEdit(event.getValue()));

		updateUserList();
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
			userAccessRoles = event.getAllSelectedItems().stream().collect(Collectors.toList());
			detachAccessRoles.setEnabled(!userAccessRoles.isEmpty());
		});

		updateAccessRoleList();
	}

	private void userEdit(User user) {
		if (user == null) {
			closeUserForm();
		} else {
			userFormView.setUser(user);
			userFormView.setVisible(true);
			this.user = user;
			addClassName(editClassName);
			userFormView.setManageRolesButtonEnabled(user.getId() != null);
		}
	}

	public void filterTextFieldConfig() {
		filterTextField.setLabel("Поиск пользователей");
		filterTextField.setPlaceholder("Введите текст...");
		filterTextField.setClearButtonVisible(true);
		filterTextField.setValueChangeMode(ValueChangeMode.LAZY);
		filterTextField.addValueChangeListener(textFieldStringComponentValueChangeEvent -> updateUserList());
		filterTextField.addValueChangeListener(event -> {
			if (filterTextField.getValue().isEmpty())
				closeUserForm();
		});
	}

	private void buttonHandler(ClickEvent<Button> buttonClickEvent) {
		User newUser = new User();
		newUser.setUsername(filterTextField.getValue());
		newUser.setEmail(filterTextField.getValue());
		newUser.setActive(true);
		userEdit(newUser);
	}

	public void createButtonEvent() {
		filterTextField.setValue("");
		userFormView.setUser(null);
		userFormView.setManageRolesButtonEnabled(false);
		updateUserList();
	}

	public void closeButtonHandler() {
		userFormView.setUser(null);
		userFormView.setManageRolesButtonEnabled(false);
		userGrid.deselectAll();
		closeUserForm();
	}

	public void manageRoleEventHandler() {
		updateAccessRoleList();
		actionBarWithGrid.setVisible(!actionBarWithGrid.isVisible());
		accessRolesLayout.setVisible(!accessRolesLayout.isVisible());

		updateUserFormElements(accessRolesLayout.isVisible());
	}

	public void updateUserFormElements(boolean accessRoleVisible) {
		if (accessRoleVisible) {
			userFormView.manageRoles.addClassName("mt-s");
			userFormView.manageRoles.setText("Пользователи");
			accessRolesTitle.setText("Роли пользователя: ");
			if (this.user != null && !this.user.getUsername().isEmpty())
				accessRolesTitle.setText(accessRolesTitle.getText() + this.user.getUsername());
		} else {
			userFormView.manageRoles.removeClassName("mt-s");
			userFormView.manageRoles.setText("Роли пользователя");
		}

		userFormView.username.setEnabled(!accessRoleVisible);
		userFormView.email.setEnabled(!accessRoleVisible);
		userFormView.password.setEnabled(!accessRoleVisible);
		userFormView.status.setEnabled(!accessRoleVisible);
		userFormView.closeButtonContainer.setVisible(!accessRoleVisible);
		userFormView.buttonsLayout.setVisible(!accessRoleVisible);
	}
}
