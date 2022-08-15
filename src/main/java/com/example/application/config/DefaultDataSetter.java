package com.example.application.config;

import com.example.application.entities.AccessRole;
import com.example.application.entities.Privilege;
import com.example.application.entities.User;
import com.example.application.services.AccessRoleService;
import com.example.application.services.PrivilegeService;
import com.example.application.services.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class DefaultDataSetter {
    private final UserService userService;
    private final PrivilegeService privilegeService;
    private final AccessRoleService accessRoleService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);

//    private User admin = new User(
//            "admin", "bck-dkiselev@yandex.ru", this.bCryptPasswordEncoder.encode("admin"),
//            true, Collections.emptyList()
//    );
//
//    private User user = new User(
//            "user", "sanddwich51@gmail.com", this.bCryptPasswordEncoder.encode("user"),
//            true, Collections.emptyList()
//    );
//
//    private User test = new User(
//            "test", "sanddwich1201@gmail.com", this.bCryptPasswordEncoder.encode("test"),
//            true, Collections.emptyList()
//    );

    private List<Privilege> privilegeList = Stream.of(
            new Privilege("ADMIN", "ADMIN", "ADMIN ACCESS"),
            new Privilege("USER", "USER", "USER ACCESS"),
            new Privilege("SECURE_PAGE", "SECURE_PAGE", "SECURE PAGE"),
            new Privilege("REST_API_GET", "REST_API_GET", "REST API GET"),
            new Privilege("REST_API_POST", "REST_API_POST", "REST API POST"),
            new Privilege("REST_API_UPDATE", "REST_API_UPDATE", "REST API UPDATE"),
            new Privilege("REST_API_DELETE", "REST_API_DELETE", "REST API DELETE")
    ).collect(Collectors.toList());

    private List<AccessRole> accessRoleList = Stream.of(
            new AccessRole("ADMIN", "ADMIN", "ADMIN ROLE", Collections.emptyList()),
            new AccessRole("USER", "USER", "USER ROLE", Collections.emptyList()),
            new AccessRole("API_FULL", "API_FULL", "API FULL ACCESS ROLE", Collections.emptyList()),
            new AccessRole("API_GET", "API_GET", "API READ ACCESS ROLE", Collections.emptyList())
    ).collect(Collectors.toList());

    public DefaultDataSetter(
            UserService userService,
            PrivilegeService privilegeService,
            AccessRoleService accessRoleService
    ) {
        this.userService = userService;
        this.privilegeService = privilegeService;
        this.accessRoleService = accessRoleService;
        System.out.println("DefaultDataSetter Begin");

        this.createUsers();

        System.out.println("############### Default Data Created...");
    }

    public void createUsers() {
        User admin = new User(
                "admin", "bck-dkiselev@yandex.ru", this.bCryptPasswordEncoder.encode("Gjlvfcnthmt1!"),
                true, Collections.emptyList()
        );

        User user = new User(
                "user", "sanddwich51@gmail.com", this.bCryptPasswordEncoder.encode("user"),
                true, Collections.emptyList()
        );

        User test = new User(
                "test", "sanddwich1201@gmail.com", this.bCryptPasswordEncoder.encode("test"),
                true, Collections.emptyList()
        );

        AccessRole adminAccessRole =
                new AccessRole("ADMIN", "ADMIN", "ADMIN ROLE", Stream.of(
                        new Privilege("ADMIN", "ADMIN", "ADMIN ACCESS"),
                        new Privilege("USER", "USER", "USER ACCESS"),
                        new Privilege("SECURE_PAGE", "SECURE_PAGE", "SECURE PAGE")
                ).collect(Collectors.toList()));

        AccessRole adminAPIAccessRole =
                new AccessRole("API_FULL", "API_FULL", "API FULL ACCESS ROLE", Stream.of(
                        new Privilege("REST_API_GET", "REST_API_GET", "REST API GET"),
                        new Privilege("REST_API_POST", "REST_API_POST", "REST API POST"),
                        new Privilege("REST_API_UPDATE", "REST_API_UPDATE", "REST API UPDATE"),
                        new Privilege("REST_API_DELETE", "REST_API_DELETE", "REST API DELETE")
                ).collect(Collectors.toList()));

        AccessRole userAccessRole =
                new AccessRole("USER", "USER", "USER ROLE", Stream.of(
                        new Privilege("USER", "USER", "USER ACCESS")
                ).collect(Collectors.toList()));

        AccessRole userApiAccessRole =
                new AccessRole("API_GET", "API_GET", "API READ ACCESS ROLE", Stream.of(
                        new Privilege("REST_API_GET", "REST_API_GET", "REST API GET")
                ).collect(Collectors.toList()));

        admin.setAccessRoles(Stream.of(
                adminAccessRole, adminAPIAccessRole
        ).collect(Collectors.toList()));

        user.setAccessRoles(Stream.of(
                userAccessRole, userApiAccessRole
        ).collect(Collectors.toList()));

        test.setAccessRoles(Stream.of(
                userAccessRole
        ).collect(Collectors.toList()));

        admin = this.createUser(admin);
        user = this.createUser(user);
        test = this.createUser(test);
    }

    public User createUser(User user) {
        user.setAccessRoles(
                user.getAccessRoles().stream()
                        .map(accessRole -> {
                            accessRole.setPrivileges(
                                    accessRole.getPrivileges().stream()
                                            .peek(this.privilegeService::save)
                                            .filter(this.privilegeService::findPrivilegeByNameOrCode)
                                            .map(this::getDBPrivilegeByCode)
                                            .collect(Collectors.toList())
                            );
                            return accessRole;
                        })
                        .peek(this.accessRoleService::save)
                        .filter(this.accessRoleService::findAccessRoleByNameOrCode)
                        .map(this::getDBAccessRoleByCode)
                        .collect(Collectors.toList())
        );

        return this.userService.save(user);
    }

    public Privilege getDBPrivilegeByCode(Privilege privilege) {
        return this.privilegeService.findByCode(privilege.getCode()).stream().findFirst().get();
    }

    public AccessRole getDBAccessRoleByCode(AccessRole accessRole) {
        return this.accessRoleService.findByCode(accessRole.getCode()).stream().findFirst().get();
    }
}
