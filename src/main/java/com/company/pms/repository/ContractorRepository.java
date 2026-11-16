package com.company.pms.repository;

import com.company.pms.entity.ContractorEmployee;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(transactionManager = "h2TransactionManager")
public class ContractorRepository {

    private final SessionFactory sessionFactory;

    public ContractorRepository(
            @Qualifier("h2SessionFactory")
            SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    // CREATE
    public ContractorEmployee save(ContractorEmployee contractor) {
        Session session = sessionFactory.getCurrentSession();
        session.persist(contractor);
        return contractor;
    }

    // CHECK EMAIL EXISTS
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {

        Session session = sessionFactory.getCurrentSession();

        Long count = session.createSelectionQuery(
                        "SELECT COUNT(c) FROM ContractorEmployee c " +
                                "WHERE LOWER(TRIM(c.email)) = LOWER(:email)",
                        Long.class
                )
                .setParameter("email", email.trim())
                .getSingleResult();

        return count > 0;
    }

    // READ BY ID
    @Transactional(readOnly = true)
    public Optional<ContractorEmployee> findById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        return Optional.ofNullable(
                session.find(ContractorEmployee.class, id)
        );
    }

    // READ ALL
    @Transactional(readOnly = true)
    public List<ContractorEmployee> findAll() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery(
                "FROM ContractorEmployee", ContractorEmployee.class
        ).getResultList();
    }

    // UPDATE
    public ContractorEmployee update(ContractorEmployee contractor) {
        Session session = sessionFactory.getCurrentSession();
        return session.merge(contractor);
    }

    // DELETE
    public void deleteById(Long id) {
        Session session = sessionFactory.getCurrentSession();

        ContractorEmployee contractor =
                session.find(ContractorEmployee.class, id);

        if (contractor != null) {
            session.remove(contractor);
        }
    }
}