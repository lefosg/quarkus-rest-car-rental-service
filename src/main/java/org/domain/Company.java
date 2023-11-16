package org.domain;

import jakarta.persistence.*;
import org.util.Money;

import java.util.Objects;

@Entity
@DiscriminatorValue("Company")
public class Company extends User{

    @Column(name="AFM", length=30)
    private String AFM;

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

    //fixme (kinda)
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)  //policy not to big... make FetchType eager
    private ChargingPolicy policy;

    public Company() { }

    public Company(String name, String email, String password, String phone, String street, String city, String zipcode, String AFM, String IBAN) {
        super(name, email, password, phone, street, city, zipcode);
        this.AFM = AFM;
        this.IBAN = IBAN;
        income = new Money(0);
        damage_cost = new Money(0);
    }

    public String getAFM() {
        return AFM;
    }

    public void setAFM(String AFM) {
        this.AFM = AFM;
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

    @Override
    public String toString() {
        return "Company{" +
                "AFM='" + AFM + '\'' +
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
                && this.AFM.equals(company.getAFM())
                && this.IBAN.equals(company.getIBAN())
                && this.income.equals(company.getIncome())
                && this.damage_cost.equals(company.getDamage_cost());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAFM(), getIBAN(), getIncome(), getDamage_cost());
    }
}
