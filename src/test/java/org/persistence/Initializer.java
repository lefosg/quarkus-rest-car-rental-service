package org.persistence;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.domain.Company;
import org.domain.Customer;
import org.util.SystemDate;

import java.time.LocalDate;

public class Initializer {

    private EntityManager em;

    public Initializer() {
        em = JPAUtil.getCurrentEntityManager();
    }

    private void eraseData() {
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        em.createNativeQuery("delete from USERS").executeUpdate();

        tx.commit();
    }

    public void prepareData() {

        eraseData();

        //create data

        LocalDate date = LocalDate.of(2022, 11, 11);
        Customer customer1 =new Customer("GObbb", "GObbb","GObbb","GObbb",
                "GObbb","GObbb","GObbb", "GObbb","GObbb",date,"GObbb");
        Customer customer2 =new Customer("GObbb", "GObbb","GObbb","GObbb",
                "GObbb","GObbb","GObbb", "GObbb","GObbb",date,"GObbb");

        Company company1 = new Company("etaireia1","123456789", "123456798", "GObbb","GObbb","GObbb","GObbb","GObbb","GObbb");
        Company company2 = new Company("etaireia2","123456789", "123456798", "GObbb","GObbb","GObbb","GObbb","GObbb","GObbb");

        //persist

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        em.persist(customer1);
        em.persist(customer2);

        em.persist(company1);
        em.persist(company2);

        tx.commit();

        em.close();



    }

}
