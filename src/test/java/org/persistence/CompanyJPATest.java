package org.persistence;

import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import jakarta.persistence.RollbackException;
import org.domain.ChargingPolicy;
import org.domain.Company;
import org.domain.Vehicle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.util.DamageType;
import org.util.Money;
import org.util.VehicleType;

import static org.junit.jupiter.api.Assertions.*;

import java.util.LinkedHashMap;
import java.util.List;

class CompanyJPATest extends JPATest{

    @Test
    public void listCompanies() {
        List<Company> companies = em.createQuery("select c from Company c").getResultList();
        assertEquals(2, companies.size());
    }

    @Test
    public void denySavingCompanyWithSameAFM() {
        Company company1 = new Company("AVIS","avis@gmail.com", "password123", "2104578965",
                "ΠΑΤΗΣΙΩΝ 37","ΑΘΗΝΑ","12478","163498317","GR2514526358789654");
        Company company2 = new Company("SPEED","speed@gmail.com", "ilovecookies",
                "2644125415","ΛΕΥΚΩΣΙΑΣ 66","ΠΑΤΡΑ","34785","163498317","GR3687254378963625");

        Assertions.assertThrows(RollbackException.class, () -> {
            EntityTransaction tx = em.getTransaction();
            tx.begin();
            em.persist(company1);
            em.persist(company2);
            tx.commit();
        });
    }

    @Test
    public void denySavingCompanyWithSameEmail() {
        Company company1 = new Company("AVIS","avis@gmail.com", "password123", "2104578965",
                "ΠΑΤΗΣΙΩΝ 37","ΑΘΗΝΑ","12478","163498317","GR2514526358789654");
        Company company2 = new Company("SPEED","avis@gmail.com", "ilovecookies",
                "2644125415","ΛΕΥΚΩΣΙΑΣ 66","ΠΑΤΡΑ","34785","999641227","GR3687254378963625");

        Assertions.assertThrows(RollbackException.class, () -> {
            EntityTransaction tx = em.getTransaction();
            tx.begin();
            em.persist(company1);
            em.persist(company2);
            tx.commit();
        });
    }

    @Test
    public void denySavingCompanyWithSameAfmAndEmail() {
        Company company1 = new Company("AVIS","avis@gmail.com", "password123", "2104578965",
                "ΠΑΤΗΣΙΩΝ 37","ΑΘΗΝΑ","12478","163498317","GR2514526358789654");
        Company company2 = new Company("SPEED","avis@gmail.com", "ilovecookies",
                "2644125415","ΛΕΥΚΩΣΙΑΣ 66","ΠΑΤΡΑ","34785","163498317","GR3687254378963625");

        Assertions.assertThrows(RollbackException.class, () -> {
            EntityTransaction tx = em.getTransaction();
            tx.begin();
            em.persist(company1);
            em.persist(company2);
            tx.commit();
        });
    }

    @Test
    public void fetchPolicyFromFirstCompany() {
        LinkedHashMap<Integer, Float> mileage_scale = new LinkedHashMap<Integer, Float>();
        mileage_scale.put(100, 0.10f);
        mileage_scale.put(200, 0.20f);
        mileage_scale.put(300, 0.30f);

        LinkedHashMap<DamageType, Float> damage_type = new LinkedHashMap<DamageType, Float>();
        damage_type.put(DamageType.Glasses,50f);
        damage_type.put(DamageType.Machine,30f);

        ChargingPolicy testPolicy = new ChargingPolicy(mileage_scale,damage_type);

        //query c.policy
        ChargingPolicy firstStored = (ChargingPolicy) em.createQuery("select c.policy from Company c").getResultList().get(0);

        assertEquals(testPolicy, firstStored);
    }

    @Test
    public void fetchVehiclesByCompanyName() {
        //we currently have only one company named "SPEED", and column not unique
        Query query = em.createQuery("select c.vehicles from Company c where c.name=:name");
        query.setParameter("name", "SPEED");
        List<Vehicle> vehicles = query.getResultList();
        assertEquals(5, vehicles.size());
    }

    @Test
    public void fetchVehiclesByCompanyAFM() {
        Query query = em.createQuery("select c.vehicles from Company c where c.AFM=:afm");
        query.setParameter("afm", "163498317");
        List<Vehicle> vehicles = query.getResultList();
        assertEquals(6, vehicles.size());
    }

    @Test
    public void fetchVehiclesByCompanyEmail() {
        Query query = em.createQuery("select c.vehicles from Company c where c.email=:email");
        query.setParameter("email", "speed@gmail.com");
        List<Vehicle> vehicles = query.getResultList();
        assertEquals(5, vehicles.size());
    }

    @Test
    public void fetchWithPolicyAndVehiclesByAFM() {
        Query query = em.createQuery("select c from Company c where c.AFM=:afm");
        query.setParameter("afm", "163498317");
        List<Company> result = query.getResultList();

        assertEquals(1, result.size());

        List<Vehicle> vehicles = result.get(0).getVehicles();
        assertEquals(6, vehicles.size());

        ChargingPolicy policy = result.get(0).getPolicy();
        assertNotNull(policy);
    }

    @Test
    public void fetchWithPolicyAndVehiclesByEmail() {
        Query query = em.createQuery("select c from Company c where c.email=:email");
        query.setParameter("email", "speed@gmail.com");
        List<Company> result = query.getResultList();

        assertEquals(1, result.size());

        List<Vehicle> vehicles = result.get(0).getVehicles();
        assertEquals(5, vehicles.size());

        ChargingPolicy policy = result.get(0).getPolicy();
        assertNotNull(policy);
    }


    @Test
    public void persistCompanyWithVehicle() {
        Vehicle newVehicle = new Vehicle("PEUGEOT", "208", 2013, 141000,
                "NAN-4067", VehicleType.Hatchback, new Money(40));

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        Query query = em.createQuery("select c from Company c where c.AFM=:afm");
        query.setParameter("afm", "163498317");  // AVIS
        List<Company> result = query.getResultList();

        Company company = result.get(0);
        company.addVehicle(newVehicle);

        tx.commit();

        assertNotNull(newVehicle.getId());

        em.close();

        em = JPAUtil.getCurrentEntityManager();

        Vehicle savedVehicle = em.find(Vehicle.class, newVehicle.getId());
        assertNotNull(savedVehicle);
        assertEquals("PEUGEOT", savedVehicle.getManufacturer());

    }

    @Test
    public void persistCompanyWithVehicleAndThenDeleteVehicle() {
        Vehicle newVehicle = new Vehicle("PEUGEOT", "208", 2013, 141000,
                "NAN-4067", VehicleType.Hatchback, new Money(40));

        //persist
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        Query query = em.createQuery("select c from Company c where c.AFM=:afm");
        query.setParameter("afm", "163498317");  // AVIS
        List<Company> result = query.getResultList();

        Company company = result.get(0);

        //before adding the vehicle, assert it has as many vehicles as it should
        assertEquals(6, company.getVehicles().size());

        company.addVehicle(newVehicle);

        tx.commit();

        assertNotNull(newVehicle.getId());

        em.close();

        //check persistence
        em = JPAUtil.getCurrentEntityManager();
        Vehicle savedVehicle = em.find(Vehicle.class, newVehicle.getId());

        assertNotNull(savedVehicle);
        assertEquals(7, company.getVehicles().size());

        //delete
        tx = em.getTransaction();
        tx.begin();

        company.removeVehicle(savedVehicle);

        tx.commit();

        em.close();

        //check if vehicle still in list (shouldn't be)
        em = JPAUtil.getCurrentEntityManager();

        assertEquals(6, company.getVehicles().size());

    }


    @Test
    public void deleteCompanyDeletesVehicles() {

        //first get the company id
        Query query = em.createQuery("select c from Company c where c.AFM=:afm");
        query.setParameter("afm", "163498317");  // AVIS
        Company company = (Company) query.getResultList().get(0);

        assertNotNull(company);
        long id = company.getId();

        //delete the company
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        em.createNativeQuery("delete from USERS u where u.AFM=163498317").executeUpdate();
        //deletion should cascade, and delete vehicles as well

        tx.commit();

        Company deletedCompany = em.find(Company.class, id);

        assertNull(deletedCompany);
//        query = em.createQuery("select v from Vehicle v left join Company c where v.company_id=:id");
//        query.setParameter("company_id", id);
//        List<Company> comp = query.getResultList();
//
//        assertEquals(0, comp.size());

    }



}