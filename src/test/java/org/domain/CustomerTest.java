package org.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.util.SystemDateStub;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {
    //todo test getters & setters -> VASO ok
    Customer customer;
    LocalDate date = LocalDate.of(2023, 11, 15);

    @BeforeEach
    public void setup() {
        SystemDateStub.setStub(date);
        customer = new Customer("GObbb",
                "GObbb","GObbb","GObbb","GObbb","GObbb","GObbb", "GObbb","GObbb",date,"GObbb");
    }

    @Test
    public void equalsSameCustomer() {
        assertEquals(customer, customer);
    }

    @Test
    public void equalsDifferentCustomers() {
        Customer customer2 = new Customer("GObbb",
                "GObbb","GObbb","GObbb","GObbb","GObbb","GObbb", "GObbb","GObbb",date,"GObbb");
        assertEquals(customer, customer2);
    }

    @Test
    public void notEqualsDifferentCustomers() {
        Customer customer2 = new Customer("GObb", "GObbb","GObbb","GObbb",
                "GObbb","GObbb","GObbb", "GObbb","GObbb",date,"GObbb");
        assertNotEquals(customer, customer2);
    }

    @Test
    public void testsetSurname() {
        assertEquals("GObbb", customer.getSurname());
    }

    @Test
    public void testsetNumber() {
        assertEquals("GObbb", customer.getNumber());
    }

    @Test
    public void testsetExpirationDate() {
        assertEquals( date, customer.getExpirationDate());
    }

    @Test
   public void testsetHolderName() {
        customer.setHolderName("GObbb");
        assertEquals("GObbb", customer.getHolderName());

    }

    @Test
    public void testgetSurname() {
        assertEquals("GObbb", customer.getSurname());
    }

    @Test
    public void testgetNumber() {
        assertEquals("GObbb", customer.getNumber());

    }

    @Test
    public void testgetExpirationDate() {
        assertEquals( date, customer.getExpirationDate());
    }

    @Test
    public void testgetHolderName() {
        assertEquals("GObbb", customer.getHolderName());
    }
}