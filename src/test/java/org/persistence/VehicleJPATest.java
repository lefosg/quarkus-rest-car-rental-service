package org.persistence;

import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import jakarta.persistence.RollbackException;
import org.domain.Vehicle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.domain.Customer;
import org.junit.jupiter.api.Test;
import org.util.Money;
import org.util.VehicleType;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class VehicleJPATest extends JPATest {

    @Test
    public void listVehicle() {
        List<Vehicle> vehicles = em.createQuery("select v from Vehicle v").getResultList();
        assertEquals(11, vehicles.size());
    }

    @Test
    public void validateFirstVehiclePersisted() {
        Vehicle firstStored = (Vehicle) em.createQuery("select v from Vehicle v").getResultList().get(0);
        Vehicle testVehicle = new Vehicle("TOYOTA", "YARIS", 2015, 100000,
                "YMB-6325", VehicleType.Hatchback, new Money(30));
        assertEquals(testVehicle, firstStored);
    }

    @Test
    public void denySavingWithSamePlateNumber() {
        Vehicle vehicle1 = new Vehicle("AUDI", "A7", 2021, 100000,
                "MMA-8745", VehicleType.Sedan, new Money(70));
        Vehicle vehicle2 = new Vehicle("NISSAN", "QASHQAI AUTOMATIC", 2023, 50000,
                "MMA-8745", VehicleType.SUV, new Money(100));

        Assertions.assertThrows(RollbackException.class, () -> {
            EntityTransaction tx = em.getTransaction();
            tx.begin();
            em.persist(vehicle1);
            em.persist(vehicle2);
            tx.commit();
        });
    }

    @Test
    public void fetchVehicleByPlateNumber() {
        Query query = em.createQuery("select v from Vehicle v where v.plateNumber=:plateNumber");  //plateNumber is unique
        query.setParameter("plateNumber", "PIP-4556");  //PIP-4556 is the opel corsa
        List<Vehicle> vehicles = query.getResultList();

        assertEquals(1, vehicles.size());

        String manufacturer = vehicles.get(0).getManufacturer();
        assertEquals("OPEL", manufacturer);
    }

    @Test
    public void fetchVehiclesByManufacturer() {
        Query query = em.createQuery("select v from Vehicle v where v.manufacturer=:manufacturer");  //plateNumber is unique
        query.setParameter("manufacturer", "VOLKSWAGEN");  //PIP-4556 is the opel corsa
        List<Vehicle> vehicles = query.getResultList();

        assertEquals(2, vehicles.size());
    }
}
