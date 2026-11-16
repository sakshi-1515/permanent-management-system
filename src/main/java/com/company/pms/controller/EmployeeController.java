
package com.company.pms.controller;

import com.company.pms.entity.ContractorEmployee;
import com.company.pms.entity.PermanentEmployee;
import com.company.pms.service.EmployeeRoutingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.company.pms.dto.EmployeeRequestDTO;

import java.util.Locale;


@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeRoutingService employeeRoutingService;

    public EmployeeController(
            EmployeeRoutingService employeeRoutingService) {
        this.employeeRoutingService = employeeRoutingService;
    }


    @PostMapping
    public ResponseEntity<?> createEmployee(
            @Valid @RequestBody EmployeeRequestDTO request) {

        String employeeType = request.getEmployeeType();

        Object employee;

        if ("PERMANENT".equals(employeeType)) {
            PermanentEmployee permanent = new PermanentEmployee();
            permanent.setName(request.getName());
            permanent.setEmail(request.getEmail());
            permanent.setDepartment(request.getDepartment());
            permanent.setDesignation(request.getDesignation());
            permanent.setSalary(request.getSalary());

            employee = permanent;

        } else if ("CONTRACTOR".equals(employeeType)) {
            ContractorEmployee contractor = new ContractorEmployee();
            contractor.setName(request.getName());
            contractor.setEmail(request.getEmail());
            contractor.setDepartment(request.getDepartment());
            contractor.setDesignation(request.getDesignation());
            contractor.setSalary(request.getSalary());

            employee = contractor;

        } else {
            return ResponseEntity.badRequest()
                    .body("Employee type must be PERMANENT or CONTRACTOR");
        }

        Object savedEmployee =
                employeeRoutingService.save(employeeType, employee);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedEmployee);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEmployeeById(
            @PathVariable Long id,
            @RequestParam String employeeType) {

        try {
            Object employee =
                    employeeRoutingService.findById(employeeType, id);

            return ResponseEntity.ok(employee);

        } catch (IllegalArgumentException ex) {
            if (ex.getMessage() != null
                    && ex.getMessage().startsWith("Employee type")) {
                return ResponseEntity.badRequest().body(ex.getMessage());
            }

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ex.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllEmployees(
            @RequestParam String employeeType) {

        try {
            return ResponseEntity.ok(
                    employeeRoutingService.findAll(employeeType));

        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest()
                    .body(ex.getMessage());
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateEmployee(
            @PathVariable Long id,
            @RequestParam String employeeType,
            @Valid @RequestBody EmployeeRequestDTO request) {

        if (request.getEmployeeType() == null) {
            return ResponseEntity.badRequest()
                    .body("Employee type is required in request body");
        }

        // Convert both values to uppercase
        String normalizedType =
                employeeType.trim().toUpperCase(Locale.ROOT);

        String bodyType =
                request.getEmployeeType().trim().toUpperCase(Locale.ROOT);

        // Compare normalized values
        if (!normalizedType.equals(bodyType)) {
            return ResponseEntity.badRequest()
                    .body("Employee type in query and body must match");
        }

        try {
            Object employee;

            if ("PERMANENT".equals(normalizedType)) {

                PermanentEmployee permanent = new PermanentEmployee();
                permanent.setName(request.getName());
                permanent.setEmail(request.getEmail());
                permanent.setDepartment(request.getDepartment());
                permanent.setDesignation(request.getDesignation());
                permanent.setSalary(request.getSalary());

                employee = permanent;

            } else if ("CONTRACTOR".equals(normalizedType)) {

                ContractorEmployee contractor = new ContractorEmployee();
                contractor.setName(request.getName());
                contractor.setEmail(request.getEmail());
                contractor.setDepartment(request.getDepartment());
                contractor.setDesignation(request.getDesignation());
                contractor.setSalary(request.getSalary());

                employee = contractor;

            } else {
                return ResponseEntity.badRequest()
                        .body("Employee type must be PERMANENT or CONTRACTOR");
            }

            Object updatedEmployee =
                    employeeRoutingService.update(
                            normalizedType, id, employee);

            return ResponseEntity.ok(updatedEmployee);

        } catch (IllegalArgumentException ex) {

            if (ex.getMessage() != null
                    && ex.getMessage().startsWith("Employee type")) {
                return ResponseEntity.badRequest().body(ex.getMessage());
            }

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEmployee(
            @PathVariable Long id,
            @RequestParam String employeeType) {

        try {
            employeeRoutingService.deleteById(employeeType, id);
            return ResponseEntity.noContent().build();

        } catch (IllegalArgumentException ex) {
            if (ex.getMessage() != null
                    && ex.getMessage().startsWith("Employee type")) {
                return ResponseEntity.badRequest().body(ex.getMessage());
            }

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ex.getMessage());
        }
    }


}
