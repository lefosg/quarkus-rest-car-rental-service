package org.old.jpa;

import jakarta.persistence.EntityTransaction;
import jakarta.persistence.RollbackException;
import org.domain.Customer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class CustomerJPATest extends JPATest{

    @Test
    public void listCustomers() {
        List<Customer> customers = em.createQuery("select c from Customer c").getResultList();
        assertEquals(2, customers.size());
    }

    @Test
    public void denySavingCustomerWithSameAFM() {
        LocalDate date1 = LocalDate.of(2027, 11, 26);
        Customer customer1 =new Customer("ΙΩΑΝΝΗΣ", "evangellou@gmail.com","johnjohn","6941603677",
                "166008282","ΛΕΥΚΑΔΟΣ 22","ΑΘΗΝΑ","35896", "ΕΥΑΓΓΕΛΟΥ","7894665213797564",date1,"ΙΩΑΝΝΗΣ ΕΥΑΓΓΕΛΟΥ");
        LocalDate date2 = LocalDate.of(2026, 8, 5);
        Customer customer2 =new Customer("ΝΙΚΟΣ", "nick7@yahoo.gr", "olympiakos","6924567813",
                "166008282", "ΜΕΘΟΝΗΣ 6","ΠΕΙΡΑΙΑΣ","18545", "ΠΑΠΑΔΗΜΗΤΡΙΟΥ","1645923557481658",date2,"ΝΙΚΟΣ ΠΑΠΑΔΗΜΗΤΡΙΟΥ");

        Assertions.assertThrows(RollbackException.class, () -> {
            EntityTransaction tx = em.getTransaction();
            tx.begin();
            em.persist(customer1);
            em.persist(customer2);
            tx.commit();
        });
    }

    @Test
    public void denySavingCustomerWithSameEmail() {
        LocalDate date1 = LocalDate.of(2027, 11, 26);
        Customer customer1 =new Customer("ΙΩΑΝΝΗΣ", "evangellou@gmail.com","johnjohn","6941603677",
                "166008282","ΛΕΥΚΑΔΟΣ 22","ΑΘΗΝΑ","35896", "ΕΥΑΓΓΕΛΟΥ","7894665213797564",date1,"ΙΩΑΝΝΗΣ ΕΥΑΓΓΕΛΟΥ");
        LocalDate date2 = LocalDate.of(2026, 8, 5);
        Customer customer2 =new Customer("ΝΙΚΟΣ", "evangellou@gmail.com", "olympiakos","6924567813",
                "054893175", "ΜΕΘΟΝΗΣ 6","ΠΕΙΡΑΙΑΣ","18545", "ΠΑΠΑΔΗΜΗΤΡΙΟΥ","1645923557481658",date2,"ΝΙΚΟΣ ΠΑΠΑΔΗΜΗΤΡΙΟΥ");

        Assertions.assertThrows(RollbackException.class, () -> {
            EntityTransaction tx = em.getTransaction();
            tx.begin();
            em.persist(customer1);
            em.persist(customer2);
            tx.commit();
        });
    }

    @Test
    public void denySavingCustomerWithSameAfmAndEmail() {
        LocalDate date1 = LocalDate.of(2027, 11, 26);
        Customer customer1 =new Customer("ΙΩΑΝΝΗΣ", "evangellou@gmail.com","johnjohn","6941603677",
                "166008282","ΛΕΥΚΑΔΟΣ 22","ΑΘΗΝΑ","35896", "ΕΥΑΓΓΕΛΟΥ","7894665213797564",date1,"ΙΩΑΝΝΗΣ ΕΥΑΓΓΕΛΟΥ");
        LocalDate date2 = LocalDate.of(2026, 8, 5);
        Customer customer2 =new Customer("ΝΙΚΟΣ", "evangellou@gmail.com", "olympiakos","6924567813",
                "166008282", "ΜΕΘΟΝΗΣ 6","ΠΕΙΡΑΙΑΣ","18545", "ΠΑΠΑΔΗΜΗΤΡΙΟΥ","1645923557481658",date2,"ΝΙΚΟΣ ΠΑΠΑΔΗΜΗΤΡΙΟΥ");

        Assertions.assertThrows(RollbackException.class, () -> {
            EntityTransaction tx = em.getTransaction();
            tx.begin();
            em.persist(customer1);
            em.persist(customer2);
            tx.commit();
        });
    }

}