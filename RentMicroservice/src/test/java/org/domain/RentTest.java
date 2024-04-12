package org.domain;

import io.quarkus.test.junit.QuarkusTest;
import org.domain.Rents.Rent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.util.DamageType;
import org.util.Money;
import org.util.TechnicalCheckStub;
import org.util.VehicleType;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.LinkedHashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@QuarkusTest
class RentTest {

    Rent rent;
    LocalDate startDate, endDate;

    Integer vehicleId;
    Integer customerId;
    Integer rentId;

    @BeforeEach
    public void setup() {
        startDate = LocalDate.of(2023, 10, 5);
        endDate = LocalDate.of(2023, 10, 10);

        vehicleId = 1500;
        customerId = 1000;
        rentId = 4000;
        rent = new Rent(rentId,startDate, endDate, vehicleId, customerId);
        rent.setTechnicalCheck(new TechnicalCheckStub(rent));

    }

    @Test
    public void getDurationInDays() {
        assertEquals(6, rent.getDurationInDays());
    }

    @Test
    public void getDurationInDaysSameDate() {
        Rent rent = new Rent(rentId,startDate, startDate, vehicleId, customerId);
        assertEquals(1, rent.getDurationInDays());
    }

    @Test
    public void setStartDateTestInvalid(){
        assertThrows(RuntimeException.class, () -> {rent.setStartDate(LocalDate.now().minusDays(1));});
    }

}