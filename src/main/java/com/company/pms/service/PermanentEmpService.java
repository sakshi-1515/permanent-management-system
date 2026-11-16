package com.company.pms.service;

import com.company.pms.entity.PermanentEmployee;
import com.company.pms.repository.ContractorRepository;
import com.company.pms.repository.PermanentEmpRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class PermanentEmpService
        implements EmployeeService<PermanentEmployee> {

    private final PermanentEmpRepository permanentEmpRepository;
    private final ContractorRepository contractorRepository;

    public PermanentEmpService(
            PermanentEmpRepository permanentEmpRepository,
            ContractorRepository contractorRepository) {

        this.permanentEmpRepository = permanentEmpRepository;
        this.contractorRepository = contractorRepository;
    }

    // CREATE
    @Override
    public PermanentEmployee save(PermanentEmployee employee) {

        String email = employee.getEmail().trim();
        employee.setEmail(email);

        boolean permanentExists =
                permanentEmpRepository.existsByEmail(email);

        boolean contractorExists =
                contractorRepository.existsByEmail(email);

        if (permanentExists || contractorExists) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Email already exists. Please use a different email address."
            );
        }

        return permanentEmpRepository.save(employee);
    }

    // READ BY ID
    @Override
    public PermanentEmployee findById(Long id) {
        return permanentEmpRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Permanent employee not found with ID: " + id));
    }

    // READ ALL
    @Override
    public List<PermanentEmployee> findAll() {
        return permanentEmpRepository.findAll();
    }

    // UPDATE
    @Override
    public PermanentEmployee update(
            Long id, PermanentEmployee employee) {
        return permanentEmpRepository.update(id, employee)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Permanent employee not found with ID: " + id));
    }

    // DELETE
    @Override
    public void deleteById(Long id) {
        boolean deleted = permanentEmpRepository.deleteById(id);

        if (!deleted) {
            throw new IllegalArgumentException(
                    "Permanent employee not found with ID: " + id);
        }
    }
}