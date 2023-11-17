package org.persistence;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.domain.ChargingPolicy;
import org.domain.Company;
import org.domain.Customer;
import org.domain.VehicleType;

import java.time.LocalDate;
import java.util.HashMap;

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
        //em.createNativeQuery("delete from CHARGING_POLICIES").executeUpdate();


        tx.commit();
    }

    public void prepareData() {

        eraseData();
        HashMap<Integer, Float> hash1=new HashMap<>();
        HashMap<VehicleType, Integer> hash2= new HashMap<>();
        hash1.put(55, 2.5F);
        hash2.put(VehicleType.SUV,5);

        //create data

        //1a. customers
        LocalDate date = LocalDate.of(2022, 11, 11);
        Customer customer1 =new Customer("GObbb", "GObbb","GObbb","GObbb",
                "GObbb","GObbb","GObbb", "GObbb","GObbb",date,"GObbb");
        Customer customer2 =new Customer("GObbb", "GObbb","GObbb","GObbb",
                "GObbb","GObbb","GObbb", "GObbb","GObbb",date,"GObbb");

        //1b. companies
        Company company1 = new Company("etaireia1","123456789", "123456798", "GObbb","GObbb","GObbb","GObbb","GObbb","GObbb",hash1,hash2);
        Company company2 = new Company("etaireia2","123456789", "123456798", "GObbb","GObbb","GObbb","GObbb","GObbb","GObbb",hash1,hash2);

        //2. policies
        //ChargingPolicy policy1 = new ChargingPolicy();


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
