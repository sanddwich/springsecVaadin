package com.example.application.services;

import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;

public interface BaseDataService<T> {
    List<T> findAll();
    List<T> search(String searchTerm);
    T save(T entity) ;
    boolean delete(T entity) throws DataIntegrityViolationException;
}
