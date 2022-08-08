package com.example.application.repositories;

import com.example.application.entities.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege, Integer> {
    @Query("select p from Privilege p " +
            "where lower(p.name) like lower(concat('%', :searchTerm, '%')) " +
            "or lower(p.code) like lower(concat('%', :searchTerm, '%')) " +
            "or lower(p.description) like lower(concat('%', :searchTerm, '%')) "
    )
    List<Privilege> search(String searchTerm);

    List<Privilege> findByName(String name);

    List<Privilege> findByCode(String code);

    List<Privilege> findByDescription(String description);

}
