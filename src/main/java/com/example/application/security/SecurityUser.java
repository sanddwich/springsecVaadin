package com.example.application.security;

import com.example.application.entities.AccessRole;
import com.example.application.entities.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class SecurityUser implements UserDetails {

    private final String username;
    private final String password;
    private final List<SimpleGrantedAuthority> authorities;
    private final boolean isActive;

    public SecurityUser(String username, String password, List<SimpleGrantedAuthority> authorities, boolean isActive) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.isActive = isActive;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.isActive;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.isActive;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.isActive;
    }

    @Override
    public boolean isEnabled() {
        return this.isActive;
    }

    public static UserDetails fromUser(User user) {
//        Set<SimpleGrantedAuthority> simpleGrantedAuthorities = user.getAccessRoles()
//                .stream()
//                .map(AccessRole::getPrivileges)
//                .flatMap(Collection::stream)
//                .map(privilege -> new SimpleGrantedAuthority(privilege.getCode()))
//                .collect(Collectors.toSet());
//
//        simpleGrantedAuthorities.forEach(simpleGrantedAuthority -> {
//            System.out.println(simpleGrantedAuthority.getAuthority());
//        });

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.isActive(),
                user.isActive(),
                user.isActive(),
                user.isActive(),
                user.getAccessRoles()
                        .stream()
                        .map(AccessRole::getPrivileges)
                        .flatMap(Collection::stream)
                        .map(privilege -> new SimpleGrantedAuthority(privilege.getCode()))
                        .collect(Collectors.toSet())
        );
    }

}

