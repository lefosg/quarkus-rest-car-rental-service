package org.persistence;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
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

        tx.commit();
    }

    public void prepareData() {

        eraseData();

        //create data

        LocalDate date = SystemDate.now();
        Customer customer1 =new Customer("GObbb","GObbb",date,"aa", "GObbb","GObbb","GObbb","GObbb","GObbb","GObbb","GObbb");



        //persist

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        em.persist(customer1);


        tx.commit();

        em.close();



    }

}
