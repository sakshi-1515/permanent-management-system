package com.company.pms.repository;

import com.company.pms.entity.PermanentEmployee;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(transactionManager = "postgresTransactionManager")
public class PermanentEmpRepositoryImpl implements PermanentEmpRepository {

    private final SessionFactory sessionFactory;

    public PermanentEmpRepositoryImpl(
            @Qualifier("postgresSessionFactory")
            SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    // CREATE
    @Override
    public PermanentEmployee save(PermanentEmployee employee) {
        Session session = sessionFactory.getCurrentSession();
        session.persist(employee);
        return employee;

    }

    // CHECK EMAIL EXISTS
    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {

        Session session = sessionFactory.getCurrentSession();

        Long count = session.createSelectionQuery(
                        "SELECT COUNT(e) FROM PermanentEmployee e " +
                                "WHERE LOWER(TRIM(e.email)) = LOWER(:email)",
                        Long.class
                )
                .setParameter("email", email.trim())
                .getSingleResult();


        return count > 0;
    }

    // READ BY ID
    @Override
    @Transactional(readOnly = true)
    public Optional<PermanentEmployee> findById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        return Optional.ofNullable(
                session.find(PermanentEmployee.class, id)
        );
    }

    // READ ALL
    @Override
    @Transactional(readOnly = true)
    public List<PermanentEmployee> findAll() {
        Session session = sessionFactory.getCurrentSession();
        return session.createSelectionQuery(
                "FROM PermanentEmployee",
                PermanentEmployee.class
        ).getResultList();
    }

    // UPDATE
    @Override
    public Optional<PermanentEmployee> update(
            Long id, PermanentEmployee updatedEmployee) {

        Session session = sessionFactory.getCurrentSession();

        PermanentEmployee existing =
                session.find(PermanentEmployee.class, id);

        if (existing == null) {
            return Optional.empty();
        }

        existing.setName(updatedEmployee.getName());
        existing.setEmail(updatedEmployee.getEmail());
        existing.setDepartment(updatedEmployee.getDepartment());
        existing.setDesignation(updatedEmployee.getDesignation());
        existing.setSalary(updatedEmployee.getSalary());

        return Optional.of(existing);
    }

    // DELETE
    @Override
    public boolean deleteById(Long id) {
        Session session = sessionFactory.getCurrentSession();

        PermanentEmployee employee =
                session.find(PermanentEmployee.class, id);

        if (employee == null) {
            return false;
        }

        session.remove(employee);
        return true;
    }
}