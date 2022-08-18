package com.example.application.views.components.notification;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;

public class ErrorNotification {
	public static void showNotification(String message, boolean error) {
		Notification notification = new Notification();
		notification.setDuration(5000);

		notification.addThemeVariants(
		  error
			? NotificationVariant.LUMO_ERROR
			: NotificationVariant.LUMO_PRIMARY
		);
		notification.setText(message);

		notification.open();
	}
}
