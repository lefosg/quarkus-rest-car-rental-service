package org.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.util.SystemDateStub;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    Customer customer;
    LocalDate date = LocalDate.of(2023, 11, 15);

    @BeforeEach
    public void setup() {
        SystemDateStub.setStub(date);
        LocalDate date1 = LocalDate.of(2027, 11, 26);
        customer = new Customer("ΙΩΑΝΝΗΣ", "evangellou@gmail.com", "johnjohn", "6941603677",
                "166008282", "ΛΕΥΚΑΔΟΣ 22", "ΑΘΗΝΑ", "35896", "ΕΥΑΓΓΕΛΟΥ", "7894665213797564", date1, "ΙΩΑΝΝΗΣ ΕΥΑΓΓΕΛΟΥ");

    }

    @Test
    public void equalsCustomerSameReference() {
        assertEquals(customer, customer);
    }

    @Test
    public void equalsCustomerDifferentReference() {
        LocalDate date = LocalDate.of(2027, 11, 26);
        Customer customer2 = new Customer("ΙΩΑΝΝΗΣ", "evangellou@gmail.com", "johnjohn", "6941603677",
                "166008282", "ΛΕΥΚΑΔΟΣ 22", "ΑΘΗΝΑ", "35896", "ΕΥΑΓΓΕΛΟΥ", "7894665213797564", date, "ΙΩΑΝΝΗΣ ΕΥΑΓΓΕΛΟΥ");
        assertEquals(customer, customer2);
    }

    @Test
    public void notEqualsCustomerDifferentReference() {
        LocalDate date = LocalDate.of(2026, 8, 5);
        Customer customer2 = new Customer("ΝΙΚΟΣ", "evangellou@gmail.com", "olympiakos", "6924567813",
                "166008282", "ΜΕΘΟΝΗΣ 6", "ΠΕΙΡΑΙΑΣ", "18545", "ΠΑΠΑΔΗΜΗΤΡΙΟΥ", "1645923557481658", date, "ΝΙΚΟΣ ΠΑΠΑΔΗΜΗΤΡΙΟΥ");
        assertNotEquals(customer, customer2);
    }


    @Test
    void rent() {
    }

    @Test
    void returnVehicle() {
    }

    @Test
    void pay() {
    }





    // getters & setters

    @Test
    public void testSetSurname() {
        customer.setSurname("ΠΑΠΑΓΕΩΡΓΙΟΥ");
        assertEquals("ΠΑΠΑΓΕΩΡΓΙΟΥ", customer.getSurname());
    }

    @Test
    public void testSetNumber() {
        customer.setNumber("1234665213797564");
        assertEquals("1234665213797564", customer.getNumber());
    }

    @Test
    public void testSetExpirationDate() {
        LocalDate date = LocalDate.of(2025, 10, 2);
        customer.setExpirationDate(date);
        assertEquals(date, customer.getExpirationDate());
    }

//todo: vaso

    @Test
    public void testSetHolderName() {
        customer.setHolderName("ΝΙΚΟΣ ΠΑΠΑΔΗΜΗΤΡΙΟΥ");
        assertEquals("ΝΙΚΟΣ ΠΑΠΑΔΗΜΗΤΡΙΟΥ", customer.getHolderName());
    }

    @Test
    public void testGetSurname() {
        assertEquals("ΕΥΑΓΓΕΛΟΥ", customer.getSurname());
    }


    @Test
    public void testGetNumber() {
        assertEquals("7894665213797564", customer.getNumber());
    }

    @Test
    public void testGetHolderName() {
        assertEquals("ΙΩΑΝΝΗΣ ΕΥΑΓΓΕΛΟΥ", customer.getHolderName());
    }


}