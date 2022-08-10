package com.example.application.security;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Component
public class SecurityService {
    @Value("${params.LOGIN_URL}")
    private String LOGOUT_SUCCESS_URL;

    public UserDetails getAuthenticatedUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        Object principal = context.getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return (UserDetails) context.getAuthentication().getPrincipal();
        }
        // Anonymous or no authentication.
        return null;
    }

    public static boolean isAuthenticated() {
        VaadinServletRequest request = VaadinServletRequest.getCurrent();
        return request != null && request.getUserPrincipal() != null;
    }

    public static boolean authenticate(String username, String password) {
        VaadinServletRequest request = VaadinServletRequest.getCurrent();
        if (request == null) {
            // This is in a background thread and we cannot access the request to
            // log in the user
            return false;
        }
        try {
            request.login(username, password);
            return true;
        } catch (ServletException e) {
            // login exception handle code omitted
            return false;
        }
    }

//    public static boolean isAccessGranted(Class<?> securedClass) {
//        // Allow if no roles are required.
//        Guarded guarded = AnnotationUtils.findAnnotation(securedClass, Guarded.class);
//        if (guarded == null) {
//            return true; // (1)
//        }
//
//        // lookup needed role in user roles
//        List<String> allowedPrivileges = Arrays.asList(guarded.value());
//        Authentication userAuthentication = SecurityContextHolder.getContext().getAuthentication();
//
//        return userAuthentication.getAuthorities().stream() // (2)
//          .map(GrantedAuthority::getAuthority)
//          .anyMatch(allowedPrivileges::contains);
//    }
//
//    public static boolean isUserLoggedIn() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        return authentication != null
//          && !(authentication instanceof AnonymousAuthenticationToken) // (3)
//          && authentication.isAuthenticated();
//    }

    public void logout() {
        UI.getCurrent().getPage().setLocation(LOGOUT_SUCCESS_URL);
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(
                VaadinServletRequest.getCurrent().getHttpServletRequest(), null,
                null);
    }
}
