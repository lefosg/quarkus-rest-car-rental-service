package org.domain;

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
//
//        customer = createCustomer();
//        vehicle = createVehicle();
//        company = createCompany();
//
//        company.addVehicle(vehicle);
//        vehicle.setCompany(company);
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

    //three next methods are private methods in Rent, however we want to test them
//    @Test
//    public void calculateFixedCost() throws Exception {
//        Method calculateFixedCost = Rent.class.getDeclaredMethod("calculateFixedCost");
//        calculateFixedCost.setAccessible(true);
//        calculateFixedCost.invoke(rent);
//        assertEquals(new Money(180), rent.getFixedCost());
//    }
//
//    @Test
//    public void calculateMileageCost() throws Exception {
//        Method calculateMileageCost = Rent.class.getDeclaredMethod("calculateMileageCost", float.class);
//        calculateMileageCost.setAccessible(true);
//        float miles = 150;
//        calculateMileageCost.invoke(rent, miles);
//        assertEquals(new Money(20), rent.getMileageCost());
//    }

//    @Test
//    public void calculateDamageCost() throws Exception {
//        rent.setTechnicalCheck(new TechnicalCheckStub(rent));
//
//        Method calculateDamageCost = Rent.class.getDeclaredMethod("calculateDamageCost");
//        calculateDamageCost.setAccessible(true);
//
//        calculateDamageCost.invoke(rent);  //NoDamage
//        assertEquals(new Money(0), rent.getDamageCost());
//        calculateDamageCost.invoke(rent);  //Tyres
//        assertEquals(new Money(60), rent.getDamageCost());
//        calculateDamageCost.invoke(rent);  //Machine
//        assertEquals(new Money(70), rent.getDamageCost());
//        calculateDamageCost.invoke(rent);  //Glasses
//        assertEquals(new Money(50), rent.getDamageCost());
//        calculateDamageCost.invoke(rent);  //Scratches
//        assertEquals(new Money(10), rent.getDamageCost());
//        calculateDamageCost.invoke(rent);  //Interior
//        assertEquals(new Money(20), rent.getDamageCost());
//    }

//  todo den ksero ti na kano edo

//    @Test
//    public void calculateCosts() {
//        rent.setTechnicalCheck(new TechnicalCheckStub(rent));
//        float miles = 150;
//
//        rent.calculateCosts(miles);  //NoDamage, MileageCost: 20 + FixedCost: 180 = 200
//        Money total = rent.getTotalCost();
//        assertEquals(new Money(200), total);
//
//        rent.calculateCosts(miles);  //Tyres, MileageCost: 20 + FixedCost: 180 + DamageCost: 60  = 260
//        total = rent.getTotalCost();
//        assertEquals(new Money(260), total);
//
//        rent.calculateCosts(miles);  //Machine, .. + DamageCost: 70 = 270
//        total = rent.getTotalCost();
//        assertEquals(new Money(270), total);
//
//        rent.calculateCosts(miles);  //Glasses
//        total = rent.getTotalCost();
//        assertEquals(new Money(250), total);
//
//        rent.calculateCosts(miles);  //Scratches
//        total = rent.getTotalCost();
//        assertEquals(new Money(210), total);
//
//        rent.calculateCosts(miles);  //Interior
//        total = rent.getTotalCost();
//        assertEquals(new Money(220), total);
//    }

//    private Vehicle createVehicle() {
//        return new Vehicle("TOYOTA", "YARIS", 2015, 100000,
//                "YMB-6325", VehicleType.Hatchback, new Money(30));
//    }
//
//    private Customer createCustomer() {
//        return new Customer("ΙΩΑΝΝΗΣ", "evangellou@gmail.com","johnjohn","6941603677",
//                "166008282","ΛΕΥΚΑΔΟΣ 22","ΑΘΗΝΑ","35896", "ΕΥΑΓΓΕΛΟΥ",
//                "7894665213797564", LocalDate.of(2022,10,15),"ΙΩΑΝΝΗΣ ΕΥΑΓΓΕΛΟΥ");
//
//    }
//
//    private Company createCompany() {
//        Company company = new Company("AVIS","avis@gmail.com", "password123",
//                "2104578965","ΠΑΤΗΣΙΩΝ 37","ΑΘΗΝΑ","12478","163498317","GR2514526358789654");
//
//        LinkedHashMap<Integer, Float> mileage_scale = new LinkedHashMap<Integer, Float>();
//        mileage_scale.put(100, 0.10f);
//        mileage_scale.put(200, 0.20f);
//        mileage_scale.put(300, 0.30f);
//
//        LinkedHashMap<DamageType, Float> damage_type = new LinkedHashMap<DamageType, Float>();
//        damage_type.put(DamageType.NoDamage,0f);
//        damage_type.put(DamageType.Tyres,60f);
//        damage_type.put(DamageType.Machine,70f);
//        damage_type.put(DamageType.Glasses,50f);
//        damage_type.put(DamageType.Scratches,10f);
//        damage_type.put(DamageType.Interior,20f);
//
//        ChargingPolicy policy = new ChargingPolicy(mileage_scale,damage_type);
//
//        company.setPolicy(policy);
//
//        return company;
//    }

}