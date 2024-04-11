package org.infastructure.persistence;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import org.domain.customer.Customer;
import org.domain.customer.CustomerRepository;
import org.infastructure.persistence.CustomerRepositoryImpl;
import org.junit.jupiter.api.Test;
import org.util.IntegrationBase;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class CustomerRepositoryImplTest extends IntegrationBase {

    Integer custId = 1000;

    @Inject
    CustomerRepository customerRepository;

    @Test
    @Transactional
    void deleteAllCustomers() {
        customerRepository.deleteAllCustomers();
        assertEquals(0, customerRepository.listAllCustomers().size());
    }

    @Test
    @Transactional
    void deleteOneCustomerValid() {
        customerRepository.deleteCustomer(custId);
        List<Customer> customers = customerRepository.listAllCustomers();
        assertEquals(1, customers.size());
        assertNull(customerRepository.findByCustomerId(custId));
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