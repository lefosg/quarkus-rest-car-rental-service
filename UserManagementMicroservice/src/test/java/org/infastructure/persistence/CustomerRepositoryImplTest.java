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

    @Test
    @Transactional
    void findBySurnameTest() {
        assertEquals(customerRepository.findBySurname("ΠΑΠΑΔΗΜΗΤΡΙΟΥ").get(0).getId(),1001);
    }

    @Test
    @Transactional
    void findInvalidSurnameTest() {
        assertEquals(2,customerRepository.findBySurname(null).size());
    }

    @Test
    @Transactional
    void findByEmailTest() {
        assertEquals(customerRepository.findByEmail("nick7@yahoo.gr").get(0).getId(),1001);
    }

    @Test
    @Transactional
    void findInvalidEmailTest() {
        assertEquals(2,customerRepository.findByEmail(null).size());
    }

    @Test
    @Transactional
    void findByPhoneTest() {
        assertEquals(customerRepository.findByPhone("6924567813").get(0).getId(),1001);
    }

    @Test
    @Transactional
    void findInvalidPhoneTest() {
        assertEquals(2,customerRepository.findByPhone(null).size());
    }

    @Test
    @Transactional
    void findByAFMTest() {
        assertEquals(customerRepository.findByAFM("054893175").get(0).getId(),1001);
    }

    @Test
    @Transactional
    void findInvalidAFMTest() {
        assertEquals(2,customerRepository.findByAFM(null).size());
    }

    @Test
    @Transactional
    void findByNameTest() {
        assertEquals(customerRepository.findByName("ΝΙΚΟΣ").get(0).getId(),1001);
    }

    @Test
    @Transactional
    void findInvalidNameTest() {
        assertEquals(2,customerRepository.findByName(null).size());
    }
}