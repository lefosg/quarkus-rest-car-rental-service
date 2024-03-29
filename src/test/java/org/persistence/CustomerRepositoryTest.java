package org.persistence;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import org.domain.Customer;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class CustomerRepositoryTest {

    Integer custId = 1000;

    @Inject
    CustomerRepository customerRepository;

    @Test
    @Transactional
    void deleteAllCustomers() {
        customerRepository.deleteAllCustomers();
        assertEquals(0, customerRepository.listAll().size());
    }

    @Test
    @Transactional
    void deleteOneCustomerValid() {
        customerRepository.deleteCustomer(custId);
        List<Customer> customers = customerRepository.listAll();
        assertEquals(1, customers.size());
        assertNull(customerRepository.findById(custId));
    }

    @Test
    @Transactional
    void deleteOneCompanyInvalid() {
        assertThrows(IllegalArgumentException.class, () -> {
            customerRepository.deleteCustomer(null);
        });

        assertThrows(NotFoundException.class, () -> {
            customerRepository.deleteCustomer(1005);
        });
    }

}