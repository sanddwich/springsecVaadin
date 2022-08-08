package com.example.application.restControllers;

import com.example.application.services.AccessRoleService;
import com.example.application.services.PrivilegeService;
import com.example.application.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/developer")
public class RestAPIController {

    private final PrivilegeService privilegeService;
    private final AccessRoleService accessRoleService;
    private final UserService userService;

    public RestAPIController(
            PrivilegeService privilegeService,
            AccessRoleService accessRoleService,
            UserService userService
    ) {
        this.privilegeService = privilegeService;
        this.accessRoleService = accessRoleService;
        this.userService = userService;
    }

    @GetMapping
    public String index() {
        return "REST API Service";
    }

    @GetMapping("/users")
//	@PreAuthorize("hasAuthority('REST_API_GET')")
    @PreAuthorize("hasAuthority('SECURE_PAGE')")
    public ResponseEntity userList() {
        try {
            return ResponseEntity.ok(this.userService.findAll());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Get userList ERROR: " + e.getMessage());
        }
    }

    @GetMapping("/users/{id}")
    @PreAuthorize("hasAuthority('REST_API_GET')")
    public ResponseEntity getUserRole(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(this.userService.findById(id).get());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Get user ERROR: " + e.getMessage());
        }
    }

    @GetMapping("/roles")
    @PreAuthorize("hasAuthority('REST_API_GET')")
    public ResponseEntity accessRoleList() {
        try {
            return ResponseEntity.ok(this.accessRoleService.findAll());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Get accessRoleList ERROR: " + e.getMessage());
        }
    }

    @GetMapping("/roles/{id}")
    @PreAuthorize("hasAuthority('REST_API_GET')")
    public ResponseEntity getAccessRole(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(this.accessRoleService.findById(id).get());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Get accessRole ERROR: " + e.getMessage());
        }
    }

    @GetMapping("/privileges")
    @PreAuthorize("hasAuthority('REST_API_GET')")
    public ResponseEntity privilegeList() {
        try {
            return ResponseEntity.ok(this.privilegeService.findAll());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Get privilegeList ERROR: " + e.getMessage());
        }
    }

    @GetMapping("/privileges/{id}")
    @PreAuthorize("hasAuthority('REST_API_GET')")
    public ResponseEntity getPrivilege(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(this.privilegeService.findById(id).get());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Get privilege ERROR: " + e.getMessage());
        }
    }
}
