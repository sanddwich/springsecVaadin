package com.example.application.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.List;

@Entity
@Data
@Table(indexes = {
        @Index(name = "usernameIndex", columnList = "username"),
        @Index(name = "emailIndex", columnList = "email")
})
public class User extends AbstractEntity {
    public User() {
    }

    public User(@NotEmpty String username, @NotEmpty String email, @NotEmpty String password, @NotEmpty boolean active, List<AccessRole> accessRoles) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.active = active;
        this.accessRoles = accessRoles;
    }

    @NotEmpty
    @Column(unique = true)
    private String username;

    @NotEmpty
    @Email
    @Column(unique = true)
    private String email;

    @JsonIgnore
    @NotEmpty
    private String password;

    @NotNull
    private boolean active;

    @ManyToMany(fetch = FetchType.EAGER
//	  cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}
    )
    @JoinTable(
            name = "user_access_role_lnk",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")}
    )
    private List<AccessRole> accessRoles;

    @Override
    public String toString() {
        return "\nUser{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", active=" + active +
                ", accessRoles=" + accessRoles +
                '}';
    }
}
