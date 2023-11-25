package org.persistence;

import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import jakarta.persistence.RollbackException;
import org.domain.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.util.DamageType;
import org.util.Money;
import org.util.VehicleType;

import java.time.LocalDate;


import java.util.LinkedHashMap;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


public class RentJPATest extends JPATest{

    @Test
    public void getNumberOfRents() {
        List<Rent> rents = em.createQuery("select r from Rent r").getResultList();
        assertEquals(1, rents.size());
    }

    @Test
    public void getVehicleModel(){
        List<Rent> rents = em.createQuery("select r from Rent r").getResultList();
        assertEquals("YARIS",rents.get(0).getRentedVehicle().getModel());
    }

    @Test
    public void deleteOnlyTheRent(){
        List<Rent> rents = em.createQuery("select r from Rent r").getResultList();
        Vehicle vehicle = rents.get(0).getRentedVehicle();
        int id = vehicle.getId();
        // Delete the rent
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        em.createNativeQuery("delete from RENTS r where r.VEHICLE_ID = :id").setParameter("id", id);
        tx.commit();
        em.close();
        em = JPAUtil.getCurrentEntityManager();
        // Check if the vehicle still exists
        List<Vehicle> vehicle1 = em.createQuery("select v from Vehicle v where v.ID = :id").setParameter("id", id).getResultList();
        Query query = em.createQuery("select r from Rent r");
        List<Rent> newRents = query.getResultList();
        assertEquals(vehicle1.size(), 1);
        assertEquals(newRents.size(),0);

    }

}
