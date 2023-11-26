package org.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.util.*;

import java.security.InvalidParameterException;
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
        //prerequisites: must create company with a charging policy to call the Customer.pay method inside Customer.returnVehicle
        Company company = createCompany();
        Vehicle vehicle = createVehicle2();
        company.addVehicle(vehicle);
        vehicle.setCompany(company);

        float miles = 100;
        float initialVehicleMiles = vehicle.getMiles();
        LocalDate startDate = LocalDate.of(2023, 11, 10);
        LocalDate endDate = LocalDate.of(2023, 11, 15);
        customer.rent(startDate, endDate, vehicle);
        customer.returnVehicle(vehicle, miles);

        assertEquals(RentState.Finished, customer.getRents().get(0).getRentState());
        assertEquals(initialVehicleMiles+miles, customer.getRents().get(0).getRentedVehicle().getMiles());
        assertEquals(VehicleState.Available, customer.getRents().get(0).getRentedVehicle().getVehicleState());
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
        assertThrows(NullPointerException.class, () -> {
            float miles = 100;
            customer.returnVehicle(null, miles);
        });

        assertThrows(InvalidParameterException.class, () -> {
            float miles = -5;
            customer.returnVehicle(createVehicle1(), miles);
        });
    }

    @Test
    void payFixNumbers() {
        //pay directly, without calling customer.rent before
        Company company = createCompany();
        Money amount = new Money(5);
        Money damages = new Money(10);
        customer.pay(amount, damages, company);
        Money company_total = new Money(company.getIncome().getAmount() + company.getDamage_cost().getAmount());
        assertEquals(new Money(15), company_total);
    }

    @Test
    void payAfterRent() {
        //instantiate a company and a vehicle
        Company company = createCompany();
        Vehicle vehicle = createVehicle1();
        company.addVehicle(vehicle);
        vehicle.setCompany(company);

        LocalDate startDate = LocalDate.of(2023, 11, 10);
        LocalDate endDate = LocalDate.of(2023, 11, 15);
        //first make a rent and pay without damage costs
        customer.rent(startDate, endDate, vehicle);
        customer.getRents().get(0).setTechnicalCheck(new TechnicalCheckStub(customer.getRents().get(0)));
        float miles = 150;
        customer.returnVehicle(vehicle, miles);  //customer.pay is called inside here, automatically all costs are calculated (mileage: 180, fixed: 20, total: 200)

        Money compIncome = company.getIncome();
        Money compDamageCosts = company.getDamage_cost();
        assertEquals(new Money(200), compIncome);
        assertEquals(new Money(0), compDamageCosts);

        //now this customer.rent call will consider a damage type of Tyres, damage cost should be 60
        //to make it easier, for now, set company income and damage costs to zero
        company.setIncome(new Money(0));
        company.setDamage_cost(new Money(0));
        customer.rent(startDate, endDate, vehicle);
        customer.getRents().get(1).setTechnicalCheck(new TechnicalCheckStub(customer.getRents().get(1)));
        customer.returnVehicle(vehicle, miles);
        compIncome = company.getIncome();
        compDamageCosts = company.getDamage_cost();

        assertEquals(new Money(200), compIncome);
        assertEquals(new Money(60), compDamageCosts);

        //make again the same rent to see that company total money accumulates
        //now the stub should return a damage type of machine, which costs 70, so 60 from before + 70 now = 130, and the income should just double (200 before + 200 now)
        customer.rent(startDate, endDate, vehicle);
        customer.getRents().get(1).setTechnicalCheck(new TechnicalCheckStub(customer.getRents().get(1)));
        customer.returnVehicle(vehicle, miles);
        compIncome = company.getIncome();
        compDamageCosts = company.getDamage_cost();

        assertEquals(new Money(400), compIncome);
        assertEquals(new Money(130), compDamageCosts);

        ((TechnicalCheckStub) customer.getRents().get(0).getTechnicalCheck()).clear();

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
        damage_type.put(DamageType.NoDamage,0f);

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