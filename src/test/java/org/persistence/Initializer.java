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

        em.createNativeQuery("delete from TECHICAL_CHECK").executeUpdate();

        em.createNativeQuery("delete from RENTS").executeUpdate();
        //technical checks
        //rents
        // vehicles
        em.createNativeQuery("delete from VEHICLES").executeUpdate();
        // users => companies & customers
        em.createNativeQuery("delete from USERS").executeUpdate();
        // policies
        em.createNativeQuery("delete from CHARGING_POLICIES").executeUpdate();


        tx.commit();
    }

    public void prepareData() {

        eraseData();


        //create data

        //1a. customers
        LocalDate date1 = LocalDate.of(2027, 11, 26);
        Customer customer1 =new Customer("ΙΩΑΝΝΗΣ", "evangellou@gmail.com","johnjohn","6941603677",
                "166008282","ΛΕΥΚΑΔΟΣ 22","ΑΘΗΝΑ","35896", "ΕΥΑΓΓΕΛΟΥ","7894665213797564",date1,"ΙΩΑΝΝΗΣ ΕΥΑΓΓΕΛΟΥ");

        LocalDate date2 = LocalDate.of(2026, 8, 5);
        Customer customer2 =new Customer("ΝΙΚΟΣ", "nick7@yahoo.gr", "olympiakos","6924567813",
                "054893175", "ΜΕΘΟΝΗΣ 6","ΠΕΙΡΑΙΑΣ","18545", "ΠΑΠΑΔΗΜΗΤΡΙΟΥ","1645923557481658",date2,"ΝΙΚΟΣ ΠΑΠΑΔΗΜΗΤΡΙΟΥ");

        //1b. companies
        Company company1 = new Company("AVIS","avis@gmail.com", "password123", "2104578965","ΠΑΤΗΣΙΩΝ 37","ΑΘΗΝΑ","12478","163498317","GR2514526358789654");
        Company company2 = new Company("SPEED","speed@gmail.com", "ilovecookies", "2644125415","ΛΕΥΚΩΣΙΑΣ 66","ΠΑΤΡΑ","34785","999641227","GR3687254378963625");

        Vehicle vehicle1 = new Vehicle("TOYOTA", "YARIS", 2015, 100000, "YMB-6325", VehicleType.Hatchback, new Money(30));
        Vehicle vehicle2 = new Vehicle("VOLKSWAGEN", "T-ROC", 2016, 80000, "PMT-3013", VehicleType.SUV, new Money(50));
        Vehicle vehicle3 = new Vehicle("RENAULT", "MEGANE", 2018, 50000, "KIK-2160", VehicleType.Sedan, new Money(40));
        Vehicle vehicle4 = new Vehicle("MAZDA", "MX-5", 2016, 30000, "NAT-1234", VehicleType.Cabrio, new Money(50));
        Vehicle vehicle5 = new Vehicle("MINI", "ONE D", 2016, 240000, "ZZE-7852", VehicleType.Mini, new Money(40));
        Vehicle vehicle6 = new Vehicle("FIAT", "500 S", 2014, 92000, "HIK-9459", VehicleType.Hatchback, new Money(30));
        Vehicle vehicle7 = new Vehicle("OPEL", "CORSA", 2018, 64000, "PIP-4556", VehicleType.Hatchback, new Money(60));
        Vehicle vehicle8 = new Vehicle("AUDI", "A7", 2021, 100000, "MMA-8745", VehicleType.Sedan, new Money(70));
        Vehicle vehicle9 = new Vehicle("NISSAN", "QASHQAI AUTOMATIC", 2023, 50000, "ZIK-6834", VehicleType.SUV, new Money(100));
        Vehicle vehicle10 = new Vehicle("TOYOTA", "C-HR", 2022, 49000, "PAP-3333", VehicleType.SUV, new Money(80));
        Vehicle vehicle11 = new Vehicle("VOLKSWAGEN", "POLO", 2018, 73000, "NIK-9012", VehicleType.Hatchback, new Money(50));


        //2. policies
        LinkedHashMap<Integer, Float> mileage_scale = new LinkedHashMap<Integer, Float>();
        mileage_scale.put(100, 0.10f);
        mileage_scale.put(200, 0.20f);
        mileage_scale.put(300, 0.30f);

        LinkedHashMap<DamageType, Float> damage_type = new LinkedHashMap<DamageType, Float>();
        damage_type.put(DamageType.Glasses,50f);
        damage_type.put(DamageType.Machine,30f);
        damage_type.put(DamageType.Scratches,90f);
        damage_type.put(DamageType.Interior,40f);
        damage_type.put(DamageType.Tyres,60f);
        damage_type.put(DamageType.NoDamage,0f);

        ChargingPolicy policy1 = new ChargingPolicy(mileage_scale,damage_type);

        mileage_scale = new LinkedHashMap<Integer, Float>();
        mileage_scale.put(150, 0.15f);
        mileage_scale.put(250, 0.25f);
        mileage_scale.put(350, 0.35f);

        ChargingPolicy policy2 = new ChargingPolicy(mileage_scale,damage_type);

        company1.setPolicy(policy1);
        company2.setPolicy(policy2);

        // add vehicles in companies & add corresponding company to each vehicle

        //company 1: vehicles 1, 3, 5, 8, 10, 11
        company1.addVehicle(vehicle1);
        vehicle1.setCompany(company1);
        company1.addVehicle(vehicle3);
        vehicle3.setCompany(company1);
        company1.addVehicle(vehicle5);
        vehicle5.setCompany(company1);
        company1.addVehicle(vehicle8);
        vehicle8.setCompany(company1);
        company1.addVehicle(vehicle10);
        vehicle10.setCompany(company1);
        company1.addVehicle(vehicle11);
        vehicle11.setCompany(company1);

        //company 2: vehicles 2, 4, 6, 7, 9
        company2.addVehicle(vehicle2);
        vehicle2.setCompany(company2);
        company2.addVehicle(vehicle4);
        vehicle4.setCompany(company2);
        company2.addVehicle(vehicle6);
        vehicle6.setCompany(company2);
        company2.addVehicle(vehicle7);
        vehicle7.setCompany(company2);
        company2.addVehicle(vehicle9);
        vehicle9.setCompany(company2);

        customer1.rent(date2,date1,vehicle1);
        float max=500, min=50;
        float miles = (float) Math.floor(Math.random() *(max - min + 1) + min);
        customer1.returnVehicle(vehicle1, miles);


        //

//        List<Vehicle> cars = customer1.viewAvailableVehicles(date2, date1);
//        Vehicle car = cars.get(0);
//        customer1.rent(date2, date1, car);
//
//        try{
//            //for (int i=0; i<customer1.getRents())
//            Thread.sleep(4000);
//            //calculate random miles
//
//        } catch (Exception e ) {
//            e.printStackTrace();
//        }
//        float max=500, min=50;
//        float miles = (float) Math.floor(Math.random() *(max - min + 1) + min);
//        customer1.returnVehicle(car, miles);

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
