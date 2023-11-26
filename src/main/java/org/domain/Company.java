package org.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.Cascade;
import org.util.DamageType;
import org.util.Money;
import org.util.VehicleType;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Entity
@DiscriminatorValue("Company")
public class Company extends User{

    @Column(name="IBAN", length=30)
    private String IBAN;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name="amount",column=@Column(name="income_amount")),
        @AttributeOverride(name="currency",column=@Column(name="income_currency")),
    })
    private Money income;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name="amount",column=@Column(name="damage_cost_amount")),
        @AttributeOverride(name="currency",column=@Column(name="damage_cost_currency")),
    })
    private Money damage_cost;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)  //policy not to big... make FetchType eager
    private ChargingPolicy policy;

    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, mappedBy="company")
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<Vehicle> vehicles = new ArrayList<>();


    public Company() { }

    public Company(String name, String email, String password, String phone, String street, String city, String zipcode, String AFM,
                   String IBAN) {
        super(name, email, password, phone, AFM, street, city, zipcode);
        this.IBAN = IBAN;
        income = new Money(0);
        damage_cost = new Money(0);
    }

    public Company(String name, String email, String password, String phone, String street, String city, String zipcode, String AFM,
                   String IBAN, ChargingPolicy policy) {
        super(name, email, password, phone, AFM, street, city, zipcode);
        this.IBAN = IBAN;
        this.income = new Money(0);
        this.damage_cost = new Money(0);
        this.policy= policy;
    }

    // domain logic


    /**
     * Given the miles a Customer has done with a rented vehicle, it calculates the
     * mileage cost the Customer has to pay.
     * @param miles
     * @return the mileage cost
     */
    public Money calculateMileageCost(float miles) {
        if (miles < 0 ) {
            throw new IllegalArgumentException("[!] Company.calculateMileageCost: negative number in miles parameter");
        }
        float mileage_cost = policy.calculateMileageCost(miles);
        return new Money(mileage_cost);
    }

    /**
     * If a Customer has cause damage on a rented vehicle, it calculates the cost to
     * charge the Customer for the damage.
     * @param damageType
     * @return the damage cost
     */
    public Money calculateDamageCost(DamageType damageType) {
        if (damageType == null) {
            throw new NullPointerException("[!] Company.calculateDamageCost: damageType is null");
        }
        float damage_cost = policy.calculateDamageCost(damageType);
        return new Money(damage_cost);
    }


    /**
     * Calculates the fixed cost for a rented vehicle
     * @param startDate
     * @param endDate
     * @param money
     * @return the fixed cost
     * also check if the vehicle is not available at this time
     */

    public Money calculateFixedCharge(LocalDate startDate, LocalDate endDate, Money money) {
        if (startDate==null){
            throw new NullPointerException("[!] Company.calculateFixedCharge: startDate is null");
        } else if (endDate==null){
            throw new NullPointerException("[!] Company.calculateFixedCharge: endDate is null");
        } else if (money == null) {
            throw new NullPointerException("[!] Company.calculateFixedCharge: money is null");
        }
        if (startDate.isAfter(endDate)) {
            throw new RuntimeException("Έχετε δώσει μεταγενέστερη ημερομηνία έναρξης ενοικίασης");
        } else if (endDate.isBefore(startDate)) {
            throw new RuntimeException("Έχετε δώσει προγενέστερη ημερομηνία λήξης ενοικίασης");
        }

        //1. calculate #days this vehicle was rented
        int days = (int) startDate.until(endDate, ChronoUnit.DAYS)+1;

        //2. do the math
        double cost = money.getAmount() * days;
        return new Money(cost);
    }

        @Override
    public void dashboard() {

    }


    public void addVehicle(Vehicle v) {
        this.vehicles.add(v);
    }

    public void removeVehicle(Vehicle v) {
        this.vehicles.remove(v);
    }
    public void removeVehicle(int index) {
        this.vehicles.remove(index);
    }


    // getters & setters

    public  String getIBAN() {
        return IBAN;
    }

    public void setIBAN(String IBAN) {
        this.IBAN = IBAN;
    }

    public Money getIncome() {
        return income;
    }

    public void setIncome(Money income) {
        this.income = income;
    }

    public Money getDamage_cost() {
        return damage_cost;
    }

    public void setDamage_cost(Money damage_cost) {
        this.damage_cost = damage_cost;
    }

    public ChargingPolicy getPolicy() {
        return policy;
    }

    public void setPolicy(ChargingPolicy policy) {
        this.policy = policy;
    }

    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    @Override
    public String toString() {
        return "Company{" +
                "AFM='" + getAFM() + '\'' +
                ", IBAN='" + IBAN + '\'' +
                ", income=" + income +
                ", damage_cost=" + damage_cost +
                ", id=" + id +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Company company)) return false;
        return  this.getName().equals(company.getName())
                && this.getPassword().equals(company.getPassword())
                && this.getCity().equals(company.getCity())
                && this.getEmail().equals(company.getEmail())
                && this.getPhone().equals(company.getPhone())
                && this.getZipcode().equals(company.getZipcode())
                && this.getAFM().equals(company.getAFM())
                && this.IBAN.equals(company.getIBAN())
                && this.income.equals(company.getIncome())
                && this.damage_cost.equals(company.getDamage_cost());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAFM(), getIBAN(), getIncome(), getDamage_cost());
    }
}
