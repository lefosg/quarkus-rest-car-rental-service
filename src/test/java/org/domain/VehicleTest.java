package org.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.util.Money;
import org.util.VehicleType;

import static org.junit.jupiter.api.Assertions.*;

class VehicleTest {

    Vehicle vehicle;

    @BeforeEach
    public void setup() {
        vehicle = new Vehicle("TOYOTA", "YARIS", 2015, 100000,
                "YMB-6325", VehicleType.Hatchback, new Money(30));
    }

    @Test
    public void equalsVehicleSameReference() {
        assertEquals(vehicle, vehicle);
    }

    @Test
    public void equalsVehicleDifferentReference() {
        Vehicle vehicle1 = new Vehicle("TOYOTA", "YARIS", 2015, 100000,
                "YMB-6325", VehicleType.Hatchback, new Money(30));
        assertEquals(vehicle, vehicle1);
    }

    @Test
    public void notEqualsVehicleDifferentReference() {
        Vehicle vehicle1 = new Vehicle("VOLKSWAGEN", "T-ROC", 2016, 80000,
                "PMT-3013", VehicleType.SUV, new Money(50));

        assertNotEquals(vehicle, vehicle1);
    }

    @Test
    void testGetManufacturer() {
    }

    @Test
    void testSetManufacturer() {
    }

    @Test
    void testGetPlateNumber() {
    }

    @Test
    void testSetPlateNumber() {
    }

    @Test
    void testGetVehicleType() {
    }

    @Test
    void testSetVehicleType() {
    }

    @Test
    void testGetVehicleState() {
    }

    @Test
    void testSetVehicleState() {
    }

    @Test
    void testGetCount() {
    }

    @Test
    void testSetCount() {
    }

    @Test
    void testGetFixedCharge() {
    }

    @Test
    void testSetFixedCharge() {
    }

    @Test
    void testGetCompany() {
    }

    @Test
    void testSetCompany() {
    }

    @Test
    void testGetModel() {
    }

    @Test
    void testSetModel() {
    }

    @Test
    void testGetYear() {
    }

    @Test
    void testSetYear() {
    }

    @Test
    void testGetMiles() {
    }

    @Test
    void testSetMiles() {
    }

    @Test
    void testGetCountDamages() {
    }

    @Test
    void testSetCountDamages() {
    }
}