package org.domain;

import jakarta.persistence.*;
import org.util.Money;
import org.util.VehicleState;

import java.util.*;

@Entity
@DiscriminatorValue("Company")
public class Company extends User{

    //todo: return afm here

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

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy="company")
    private List<Vehicle> vehicles = new ArrayList<>();

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)  //policy not to big... make FetchType eager
    private ChargingPolicy policy;


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
        income = new Money(0);
        damage_cost = new Money(0);
        this.policy= policy;
    }

    public String getIBAN() {
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

    public void addVehicle(Vehicle v) {
        this.vehicles.add(v);
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

    //todo: check all variables -> DIMITRIS
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
