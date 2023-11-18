package org.persistence;

import org.domain.Vehicle;
import org.junit.jupiter.api.Test;
import org.domain.Customer;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class VehicleJPATest extends JPATest {

    @Test
    public void listVehicle() {
        List<Vehicle> vehicles = em.createQuery("select v from Vehicle v").getResultList();
        assertEquals(1, vehicles.size());
    }
}
