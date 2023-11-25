package org.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.util.*;

import java.time.LocalDate;
import java.util.LinkedHashMap;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    Customer customer;
    LocalDate date = LocalDate.of(2023, 11, 15);

    @BeforeEach
    public void setup() {
        SystemDateStub.setStub(date);
        LocalDate date1 = LocalDate.of(2027, 11, 26);
        customer = new Customer("ΙΩΑΝΝΗΣ", "evangellou@gmail.com", "johnjohn", "6941603677",
                "166008282", "ΛΕΥΚΑΔΟΣ 22", "ΑΘΗΝΑ", "35896", "ΕΥΑΓΓΕΛΟΥ", "7894665213797564", date1, "ΙΩΑΝΝΗΣ ΕΥΑΓΓΕΛΟΥ");

    }

    @Test
    public void equalsCustomerSameReference() {
        assertEquals(customer, customer);
    }

    @Test
    public void equalsCustomerDifferentReference() {
        LocalDate date = LocalDate.of(2027, 11, 26);
        Customer customer2 = new Customer("ΙΩΑΝΝΗΣ", "evangellou@gmail.com", "johnjohn", "6941603677",
                "166008282", "ΛΕΥΚΑΔΟΣ 22", "ΑΘΗΝΑ", "35896", "ΕΥΑΓΓΕΛΟΥ", "7894665213797564", date, "ΙΩΑΝΝΗΣ ΕΥΑΓΓΕΛΟΥ");
        assertEquals(customer, customer2);
    }

    @Test
    public void notEqualsCustomerDifferentReference() {
        LocalDate date = LocalDate.of(2026, 8, 5);
        Customer customer2 = new Customer("ΝΙΚΟΣ", "evangellou@gmail.com", "olympiakos", "6924567813",
                "166008282", "ΜΕΘΟΝΗΣ 6", "ΠΕΙΡΑΙΑΣ", "18545", "ΠΑΠΑΔΗΜΗΤΡΙΟΥ", "1645923557481658", date, "ΝΙΚΟΣ ΠΑΠΑΔΗΜΗΤΡΙΟΥ");
        assertNotEquals(customer, customer2);
    }


    @Test
    void rent() {
        LocalDate startDate = LocalDate.of(2023, 11, 10);
        LocalDate endDate = LocalDate.of(2023, 11, 15);
        Vehicle vehicle = createVehicle1();
        customer.rent(startDate, endDate, vehicle);
        assertEquals(1, customer.getRents().size());
    }

    @Test
    void rentFalseInput() {
        LocalDate startDate = LocalDate.of(2023, 11, 10);
        LocalDate endDate = LocalDate.of(2023, 11, 15);
        Vehicle vehicle = createVehicle1();

        //null input
        assertThrows(NullPointerException.class, () -> {
            customer.rent(null, endDate, vehicle);
            customer.rent(startDate, null, vehicle);
            customer.rent(startDate, endDate, null);
        });

        //invalid input
        assertThrows(RuntimeException.class, () -> {
            customer.rent(endDate, startDate, vehicle);

            vehicle.setVehicleState(VehicleState.Rented);
            customer.rent(startDate, endDate, vehicle);

            Vehicle vehicle2 = createVehicle2();
            vehicle2.setVehicleState(VehicleState.Rented);
            customer.rent(startDate, endDate, vehicle2);

        });

    }

    @Test
    void returnVehicle() {
        //must create company with charging policy to call the Customer.pay method inside Customer.returnVehicle
        Company company = createCompany();
        Vehicle vehicle = createVehicle2();

        company.addVehicle(vehicle);
        vehicle.setCompany(company);

        float miles = 100;
        LocalDate startDate = LocalDate.of(2023, 11, 10);
        LocalDate endDate = LocalDate.of(2023, 11, 15);
        customer.rent(startDate, endDate, vehicle);
        customer.returnVehicle(vehicle, miles);

        //assertEquals(RentState.Finished, customer.getRents().get(0).getRentState());
    }

    @Test
    public void returnVehicleWithoutRenting() {
        Vehicle vehicle = createVehicle2();
        float miles = 100;
        //customer.rent has not been called, so no such vehicle can be returned
        assertThrows(AssertionError.class, () -> {
            customer.returnVehicle(vehicle, miles);
        });
    }

    @Test
    void returnVehicleFalseData() {


    }

    @Test
    void pay() {
    }



    private Vehicle createVehicle1() {
        return new Vehicle("TOYOTA", "YARIS", 2015, 100000,
                "YMB-6325", VehicleType.Hatchback, new Money(30));
    }

    private Vehicle createVehicle2() {
        return new Vehicle("VOLKSWAGEN", "T-ROC", 2016, 80000,
                "PMT-3013", VehicleType.SUV, new Money(50));
    }

    private Company createCompany() {
        Company company = new Company("AVIS","avis@gmail.com", "password123",
                "2104578965","ΠΑΤΗΣΙΩΝ 37","ΑΘΗΝΑ","12478","163498317","GR2514526358789654");

        LinkedHashMap<Integer, Float> mileage_scale = new LinkedHashMap<Integer, Float>();
        mileage_scale.put(100, 0.10f);
        mileage_scale.put(200, 0.20f);
        mileage_scale.put(300, 0.30f);

        LinkedHashMap<DamageType, Float> damage_type = new LinkedHashMap<DamageType, Float>();
        damage_type.put(DamageType.Glasses,50f);
        damage_type.put(DamageType.Machine,70f);
        damage_type.put(DamageType.Tyres,60f);
        damage_type.put(DamageType.Scratches,10f);
        damage_type.put(DamageType.Interior,20f);

        ChargingPolicy policy = new ChargingPolicy(mileage_scale,damage_type);

        company.setPolicy(policy);

        return company;
    }

    // getters & setters

    @Test
    public void testSetSurname() {
        customer.setSurname("ΠΑΠΑΓΕΩΡΓΙΟΥ");
        assertEquals("ΠΑΠΑΓΕΩΡΓΙΟΥ", customer.getSurname());
    }

    @Test
    public void testSetNumber() {
        customer.setNumber("1234665213797564");
        assertEquals("1234665213797564", customer.getNumber());
    }

    @Test
    public void testSetExpirationDate() {
        LocalDate date = LocalDate.of(2025, 10, 2);
        customer.setExpirationDate(date);
        assertEquals(date, customer.getExpirationDate());
    }

//todo: vaso

    @Test
    public void testSetHolderName() {
        customer.setHolderName("ΝΙΚΟΣ ΠΑΠΑΔΗΜΗΤΡΙΟΥ");
        assertEquals("ΝΙΚΟΣ ΠΑΠΑΔΗΜΗΤΡΙΟΥ", customer.getHolderName());
    }

    @Test
    public void testGetSurname() {
        assertEquals("ΕΥΑΓΓΕΛΟΥ", customer.getSurname());
    }


    @Test
    public void testGetNumber() {
        assertEquals("7894665213797564", customer.getNumber());
    }

    @Test
    public void testGetHolderName() {
        assertEquals("ΙΩΑΝΝΗΣ ΕΥΑΓΓΕΛΟΥ", customer.getHolderName());
    }


}