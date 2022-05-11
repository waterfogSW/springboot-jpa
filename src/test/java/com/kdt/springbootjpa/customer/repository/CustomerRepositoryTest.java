package com.kdt.springbootjpa.customer.repository;

import com.kdt.springbootjpa.customer.domain.Customer;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

@Slf4j
@SpringBootTest
class CustomerRepositoryTest {

    @Autowired
    CustomerRepository repository;

    @Autowired
    EntityManagerFactory emf;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("저장 테스트")
    public void save() {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        transaction.begin();

        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("san", "kim");

        em.persist(customer); // 비영속 -> 영속 (영속화)
        transaction.commit();
    }

    @Test
    @DisplayName("조회")
    public void find() {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        transaction.begin();

        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("san", "kim");

        em.persist(customer); // 비영속 -> 영속 (영속화)
        transaction.commit();

        em.detach(customer); // 영속 -> 준영속
        final var selected = em.find(Customer.class, 1L);
        log.info("{}", selected.getFullName());
    }

    @Test
    @DisplayName("수정")
    public void modify() {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        transaction.begin();
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("san", "kim");

        em.persist(customer); // 비영속 -> 영속 (영속화)
        transaction.commit();

        transaction.begin();
        customer.setName("san", "park");
        em.persist(customer);
        transaction.commit();
    }

    @Test
    @DisplayName("삭제")
    public void delete() {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        transaction.begin();
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("san", "kim");
        em.persist(customer); // 비영속 -> 영속 (영속화)
        transaction.commit();

        transaction.begin();
        em.remove(customer);
        transaction.commit();
    }
}