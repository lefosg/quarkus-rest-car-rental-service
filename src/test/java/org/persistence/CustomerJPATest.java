package org.persistence;

import org.domain.Customer;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CustomerJPATest extends JPATest{

    @Test
    public void listCustomers() {
        List<Customer> customers = em.createQuery("select c from Customer c").getResultList();
        assertEquals(1, customers.size());
    }

}