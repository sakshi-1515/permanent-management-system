package com.company.pms.service;

import com.company.pms.entity.ContractorEmployee;
import com.company.pms.repository.ContractorRepository;
import com.company.pms.repository.PermanentEmpRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ContractorEmpService
        implements EmployeeService<ContractorEmployee> {

    private final ContractorRepository contractorRepository;
    private final PermanentEmpRepository permanentEmpRepository;

    public ContractorEmpService(
            ContractorRepository contractorRepository,
            PermanentEmpRepository permanentEmpRepository) {

        this.contractorRepository = contractorRepository;
        this.permanentEmpRepository = permanentEmpRepository;
    }

    // CREATE
    @Override
    public ContractorEmployee save(ContractorEmployee contractor) {

        String email = contractor.getEmail().trim();
        contractor.setEmail(email);

        boolean contractorExists =
                contractorRepository.existsByEmail(email);

        boolean permanentExists =
                permanentEmpRepository.existsByEmail(email);

        if (contractorExists || permanentExists) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Email already exists. Please use a different email address."
            );
        }

        return contractorRepository.save(contractor);
    }

    // READ BY ID
    @Override
    public ContractorEmployee findById(Long id) {
        return contractorRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Contractor not found with ID: " + id));
    }

    // READ ALL
    @Override
    public List<ContractorEmployee> findAll() {
        return contractorRepository.findAll();
    }

    // UPDATE
    @Override
    public ContractorEmployee update(
            Long id, ContractorEmployee contractor) {

        ContractorEmployee existing = findById(id);
        contractor.setId(existing.getId());

        return contractorRepository.update(contractor);
    }

    // DELETE
    @Override
    public void deleteById(Long id) {
        if (contractorRepository.findById(id).isEmpty()) {
            throw new IllegalArgumentException(
                    "Contractor not found with ID: " + id);
        }

        contractorRepository.deleteById(id);
    }
}