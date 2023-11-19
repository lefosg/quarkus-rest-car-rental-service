package org.persistence;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.domain.ChargingPolicy;
import org.domain.Company;
import org.domain.Customer;
import org.domain.Vehicle;
import org.util.DamageType;
import org.util.Money;
import org.util.VehicleState;
import org.util.VehicleType;

import java.time.LocalDate;
import java.util.*;

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
        //2. vehicles
        //em.createNativeQuery("delete from VEHICLES").executeUpdate();


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

        Vehicle vehicle1=new Vehicle("test",155,"test","test", VehicleType.SUV, new Money(10));

        //2. policies
        LinkedHashMap<Integer, Float> mileage_scale = new LinkedHashMap<Integer, Float>();
        mileage_scale.put(100, 0.10f);
        mileage_scale.put(200, 0.20f);
        mileage_scale.put(300, 0.30f);

        LinkedHashMap<DamageType, Float> damage_type = new LinkedHashMap<DamageType, Float>();
        damage_type.put(DamageType.Glasses,50f);
        damage_type.put(DamageType.Machine,30f);

        ChargingPolicy policy1 = new ChargingPolicy(mileage_scale,damage_type);

        mileage_scale = new LinkedHashMap<Integer, Float>();
        mileage_scale.put(150, 0.15f);
        mileage_scale.put(250, 0.25f);
        mileage_scale.put(350, 0.35f);
        ChargingPolicy policy2 = new ChargingPolicy(mileage_scale,damage_type);




        company1.setPolicy(policy1);
        company2.setPolicy(policy2);

        company1.addVehicle(vehicle1);

        //persist

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        em.persist(vehicle1);

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
