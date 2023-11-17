package org.persistence;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.domain.ChargingPolicy;
import org.domain.Company;
import org.domain.Customer;
import org.domain.VehicleType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Initializer {

    private EntityManager em;

    public Initializer() {
        em = JPAUtil.getCurrentEntityManager();
    }

    private void eraseData() {
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        //1. users => companies & customers
        em.createNativeQuery("delete from USERS").executeUpdate();
        //2. policies
        em.createNativeQuery("delete from CHARGING_POLICIES").executeUpdate();


        tx.commit();
    }

    public void prepareData() {

        eraseData();


        //create data

        //1a. customers
        LocalDate date = LocalDate.of(2022, 11, 11);
        Customer customer1 =new Customer("GObbb", "GObbb","GObbb","GObbb",
                "123","GObbb","GObbb","GObbb", "GObbb","GObbb",date,"GObbb");
        Customer customer2 =new Customer("GObbb", "GObbb", "GObbb","GObbb","GObbb",
                "234","GObbb","GObbb", "GObbb","GObbb",date,"GObbb");

        //1b. companies
        Company company1 = new Company("etaireia1","123456789", "123456798", "GObbb","GObbb","GObbb","GObbb","345","GObbb");
        Company company2 = new Company("etaireia2","123456789", "123456798", "GObbb","GObbb","GObbb","GObbb","456","GObbb");

        //2. policies
        List<Integer> miles1 = new ArrayList<>();
        miles1.add(100);
        miles1.add(200);
        miles1.add(300);
        List<Float> cost1 = new ArrayList<>();
        cost1.add(0.10f);
        cost1.add(0.20f);
        cost1.add(0.30f);
        ChargingPolicy policy1 = new ChargingPolicy(miles1, cost1);

        List<Integer> miles2 = new ArrayList<>();
        miles2.add(150);
        miles2.add(250);
        miles2.add(350);
        List<Float> cost2 = new ArrayList<>();
        cost2.add(0.15f);
        cost2.add(0.25f);
        cost2.add(0.35f);
        ChargingPolicy policy2 = new ChargingPolicy(miles2, cost2);


        company1.setPolicy(policy1);
        company2.setPolicy(policy2);


        //persist

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        em.persist(policy1);
        em.persist(policy2);

        em.persist(customer1);
        em.persist(customer2);

        em.persist(company1);
        em.persist(company2);


        tx.commit();

        em.close();

    }

}
