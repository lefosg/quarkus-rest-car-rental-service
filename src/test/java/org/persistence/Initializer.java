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

        Vehicle vehicle1 = new Vehicle("test",155,"test","test", VehicleType.SUV, new Money(10));
        Vehicle vehicle2 = new Vehicle("test",155,"test","test", VehicleType.SUV, new Money(10));

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
        vehicle1.setCompany(company1);
        company1.addVehicle(vehicle2);
        vehicle2.setCompany(company1);

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
