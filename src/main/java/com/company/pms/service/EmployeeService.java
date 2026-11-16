
package com.company.pms.service;

import java.util.List;

public interface EmployeeService<T> {

    T save(T employee);

    T findById(Long id);

    List<T> findAll();

    T update(Long id, T employee);

    void deleteById(Long id);
}
