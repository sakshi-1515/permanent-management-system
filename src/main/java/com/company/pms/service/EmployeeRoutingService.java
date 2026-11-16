
package com.company.pms.service;

import com.company.pms.entity.ContractorEmployee;
import com.company.pms.entity.PermanentEmployee;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeRoutingService {

    private final PermanentEmpService permanentEmpService;
    private final ContractorEmpService contractorEmpService;

    public EmployeeRoutingService(
            PermanentEmpService permanentEmpService,
            ContractorEmpService contractorEmpService) {
        this.permanentEmpService = permanentEmpService;
        this.contractorEmpService = contractorEmpService;
    }
    public Object save(String employeeType, Object employee) {

        employeeType = validateEmployeeType(employeeType);

        if ("PERMANENT".equals(employeeType)) {

            PermanentEmployee permanent =
                    permanentEmpService.save((PermanentEmployee) employee);

            permanent.setEmployeeType(employeeType);

            return permanent;
        }

        ContractorEmployee contractor =
                contractorEmpService.save((ContractorEmployee) employee);

        contractor.setEmployeeType(employeeType);

        return contractor;
    }

    public Object findById(String employeeType, Long id) {

        employeeType = validateEmployeeType(employeeType);

        if (id == null || id <= 0) {
            throw new IllegalArgumentException(
                    "Employee ID must be positive");
        }

        if ("PERMANENT".equals(employeeType)) {

            PermanentEmployee permanent =
                    permanentEmpService.findById(id);

            permanent.setEmployeeType(employeeType);

            return permanent;
        }

        ContractorEmployee contractor =
                contractorEmpService.findById(id);

        contractor.setEmployeeType(employeeType);

        return contractor;
    }

    public List<?> findAll(String employeeType) {

        final String validatedType = validateEmployeeType(employeeType);

        if ("PERMANENT".equals(validatedType)) {

            List<PermanentEmployee> employees =
                    permanentEmpService.findAll();

            employees.forEach(employee ->
                    employee.setEmployeeType(validatedType));

            return employees;
        }

        List<ContractorEmployee> employees =
                contractorEmpService.findAll();

        employees.forEach(employee ->
                employee.setEmployeeType(validatedType));

        return employees;
    }
    public Object update(
            String employeeType,
            Long id,
            Object employee) {

        String validatedType = validateEmployeeType(employeeType);

        if (id == null || id <= 0) {
            throw new IllegalArgumentException(
                    "Employee ID must be positive");
        }

        if ("PERMANENT".equals(validatedType)) {

            PermanentEmployee updated =
                    permanentEmpService.update(
                            id, (PermanentEmployee) employee);

            updated.setEmployeeType(validatedType);

            return updated;
        }

        ContractorEmployee updated =
                contractorEmpService.update(
                        id, (ContractorEmployee) employee);

        updated.setEmployeeType(validatedType);

        return updated;
    }
    private String validateEmployeeType(String employeeType) {
        if (employeeType == null) {
            throw new IllegalArgumentException(
                    "Employee type must be PERMANENT or CONTRACTOR");
        }

        String normalizedType = employeeType.trim().toUpperCase();

        if (!"PERMANENT".equals(normalizedType)
                && !"CONTRACTOR".equals(normalizedType)) {
            throw new IllegalArgumentException(
                    "Employee type must be PERMANENT or CONTRACTOR");
        }

        return normalizedType;
    }
    public void deleteById(String employeeType, Long id) {

        String validatedType = validateEmployeeType(employeeType);

        if (id == null || id <= 0) {
            throw new IllegalArgumentException(
                    "Employee ID must be positive");
        }

        if ("PERMANENT".equals(validatedType)) {
            permanentEmpService.deleteById(id);
            return;
        }

        contractorEmpService.deleteById(id);
    }
}
