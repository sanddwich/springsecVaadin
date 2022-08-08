package com.example.application.repositories;

import com.example.application.entities.AccessRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AccessRoleRepository extends JpaRepository<AccessRole, Integer> {
    @Query("select ar from AccessRole ar " +
            "where lower(ar.name) like lower(concat('%', :searchTerm, '%')) " +
            "or lower(ar.code) like lower(concat('%', :searchTerm, '%')) " +
            "or lower(ar.description) like lower(concat('%', :searchTerm, '%')) "
    )
    List<AccessRole> search(String searchTerm);

    List<AccessRole> findByName(String name);

    List<AccessRole> findByCode(String name);

    List<AccessRole> findByDescription(String name);
}
