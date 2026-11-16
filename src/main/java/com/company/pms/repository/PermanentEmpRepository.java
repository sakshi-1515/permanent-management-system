package com.company.pms.repository;

import com.company.pms.entity.PermanentEmployee;

import java.util.List;
import java.util.Optional;

public interface PermanentEmpRepository {

    // CREATE
    PermanentEmployee save(PermanentEmployee permanentEmployee);

    // CHECK EMAIL EXISTS
    boolean existsByEmail(String email);

    // READ BY ID
    Optional<PermanentEmployee> findById(Long id);

    // READ ALL
    List<PermanentEmployee> findAll();

    // UPDATE
    Optional<PermanentEmployee> update(
            Long id, PermanentEmployee updatedEmployee);

    // DELETE
    boolean deleteById(Long id);
}