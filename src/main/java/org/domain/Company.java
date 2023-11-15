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

    //todo: fix cascade
    @OneToOne(mappedBy="company", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private ChargingPolicy policy;

    public Company() {
    }

    public Company(String name, String password, String phone, String street, String city, String email, String zipcode, String AFM, String IBAN) {
        super(name, password, phone, street, city, email, zipcode);
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

    //todo: check all variables
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Company company)) return false;
        Company c = (Company) o;
        return  this.getName().equals(c.getName()) && this.AFM.equals(c.getAFM()) && this.IBAN.equals(c.getIBAN())
                && this.income.equals(c.getIncome())
                && this.damage_cost.equals(c.getDamage_cost());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAFM(), getIBAN(), getIncome(), getDamage_cost());
    }
}
