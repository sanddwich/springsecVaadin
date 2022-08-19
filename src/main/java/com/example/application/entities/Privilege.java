package com.example.application.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(indexes = {
        @Index(name = "nameIndex", columnList = "name"),
        @Index(name = "codeIndex", columnList = "code"),
        @Index(name = "descriptionIndex", columnList = "description")
})
public class Privilege extends AbstractEntity {
    public Privilege() {}

    public Privilege(@NotEmpty String name, @NotEmpty String code, @NotEmpty String description) {
        this.name = name;
        this.code = code;
        this.description = description;
    }

    @NotEmpty
    @Column(unique = true)
    private String name;

    @NotEmpty
    @Column(unique = true)
    private String code;

    @NotEmpty
    private String description;

    //    @JsonIgnore
//    @ManyToMany(mappedBy = "privileges")
    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "access_role_privilege_lnk",
            joinColumns = {@JoinColumn(name = "privilege_id")},
            inverseJoinColumns = {@JoinColumn(name = "access_role_id")}
    )
    private List<AccessRole> roles = new ArrayList<>();

    @Override
    public String toString() {
        return "\nPrivilege{" +
                "name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", description='" + description + '\'' +
                ", roles=" + roles +
                '}';
    }
}
