package com.example.application.services;

import com.example.application.entities.Privilege;
import com.example.application.repositories.PrivilegeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PrivilegeService implements BaseDataService<Privilege> {
    private final PrivilegeRepository privilegeRepository;

    @Autowired
    public PrivilegeService(PrivilegeRepository privilegeRepository) {
        this.privilegeRepository = privilegeRepository;
    }

    @Override
    public List<Privilege> findAll() {
        return this.privilegeRepository.findAll();
    }

    @Override
    public List<Privilege> search(String searchTerm) {
        return this.privilegeRepository.search(searchTerm);
    }

    @Override
    public Privilege save(Privilege privilege) {
        if (!this.findPrivilegeByNameOrCode(privilege)) {
            this.privilegeRepository.save(privilege);
            return this.privilegeRepository.findByCode(privilege.getCode()).stream().findFirst().get();
        }

        return null;
    }

    @Override
    public void delete(Privilege privilege) throws DataIntegrityViolationException {
        this.privilegeRepository.delete(privilege);
    }

    public boolean findPrivilegeByNameOrCode(Privilege privilege) {
        if (
                !this.privilegeRepository.findByName(privilege.getName()).isEmpty() ||
                        !this.privilegeRepository.findByCode(privilege.getCode()).isEmpty()
        ) return true;

        return false;
    }

    public List<Privilege> saveAll(List<Privilege> privileges) {
        return privileges.stream()
                .filter(this::isPrivilegeByNameNotExist)
                .peek(privilegeRepository::save)
                .collect(Collectors.toList());
    }

    public boolean isPrivilegeByNameNotExist(Privilege privilege) {
        return privilegeRepository.findByName(privilege.getName()).isEmpty();
    }

    public List<Privilege> findByName(String name) {
        return this.privilegeRepository.findByName(name);
    }

    public List<Privilege> findByCode(String code) {
        return this.privilegeRepository.findByCode(code);
    }

    public List<Privilege> findByDescription(String description) {
        return this.privilegeRepository.findByDescription(description);
    }

    public Optional<Privilege> findById(Integer id) {
        return this.privilegeRepository.findById(id);
    }
}
