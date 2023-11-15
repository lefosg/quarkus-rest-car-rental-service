package org.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.util.SystemDateStub;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {
    //todo: test getters & setters
    Customer customer;
    LocalDate date = LocalDate.of(2023, 11, 15);

    @BeforeEach
    public void setup() {
        SystemDateStub.setStub(date);
        customer = new Customer("GObbb","GObbb",date,"GObbb", "GObbb",
                "GObbb","GObbb","GObbb","GObbb","GObbb","GObbb");
    }

    @Test
    public void equalsSameCustomer() {
        assertEquals(customer, customer);
    }

    @Test
    public void equalsDifferentCustomers() {
        Customer customer2 = new Customer("GObbb","GObbb",date,"GObbb", "GObbb",
                "GObbb","GObbb","GObbb","GObbb","GObbb","GObbb");
        assertEquals(customer, customer2);
    }

    @Test
    public void notEqualsDifferentCustomers() {
        Customer customer2 = new Customer("GObb","GObbb",date,"GObbb", "GObbb",
                "GObbb","GObbb","GObbb","GObbb","GObbb","GObbb");
        assertNotEquals(customer, customer2);
    }

    @Test
    void setSurname() {
    }

    @Test
    void setNumber() {
    }

    @Test
    void setExpirationDate() {
    }

    @Test
    void setHolderName() {
    }

    @Test
    void getSurname() {
        assertEquals("GObbb", customer.getSurname());
    }

    @Test
    void getNumber() {
    }

    @Test
    void getExpirationDate() {
    }

    @Test
    void getHolderName() {
    }
}