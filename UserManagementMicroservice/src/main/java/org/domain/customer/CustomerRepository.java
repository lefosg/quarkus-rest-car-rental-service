package org.domain.customer;

import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository {

    void deleteAllCustomers();

    void deleteCustomer(Integer id);

    List<Customer> findBySurname(String surname);

    List<Customer> findByEmail(String email);

    List<Customer> findByPhone(String phone);

    List<Customer> findByAFM(String AFM);

    List<Customer> findByName(String name);

    List<Customer> listAllCustomers();

    Optional<Customer> findByIdOptional(Integer integer);

    Customer findById(Integer integer);

    void persist(Customer customer);

    EntityManager getEntityManager();
}
