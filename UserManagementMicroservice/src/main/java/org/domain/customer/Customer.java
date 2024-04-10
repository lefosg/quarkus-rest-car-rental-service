package org.domain.customer;

import jakarta.persistence.*;
import org.domain.User;
import org.domain.company.Company;
import org.util.Money;
import java.time.LocalDate;
import java.util.Objects;


@Entity
@DiscriminatorValue("Customer")
public class Customer extends User {

    @Column(name="surname", length=30)
    private String surname;

    //credit card info
    @Column(name="number", length=16)
    private String number;

    @Column(name="expirationDate", length=30)
    private LocalDate expirationDate;

    @Column(name="holderName", length=25)
    private String holderName;

    //@OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy="customer")
    //private List<Rent> rents = new ArrayList<>();

    public Customer() { }

    public Customer(String name, String email, String password, String phone, String AFM, String street, String city, String zipcode,
                    String surname, String number, LocalDate expirationDate, String holderName){
        super(name, email, password, phone, AFM, street, city, zipcode);
        this.surname = surname;
        this.number = number;
        this.expirationDate = expirationDate;
        this.holderName = holderName;
    }

    // domain logic

    /**
     * Increases the <i>Company.income</i> (and depending on the case, <i>Company.damage_cost</i>) by <i>amount</i>.
     * @param amount
     * @param damages
     * @param company
     */
    public void pay(Money amount, Money damages, Company company) {
        //input check
        if (amount == null) {
            throw new NullPointerException("[!] Customer.pay: money amount is null");
        } else if (company == null) {
            throw new NullPointerException("[!] Customer.pay: damages amount is null");
        } else if (damages == null) {
            throw new NullPointerException("[!] Customer.pay: company is null");
        }
        double amountValue = amount.getAmount();
        Money newCompanyIncome = new Money(company.getIncome().getAmount() + amountValue);
        company.setIncome(newCompanyIncome);

        double damageAmountValue = damages.getAmount();
        Money newCompanyDamageCosts = new Money(company.getDamage_cost().getAmount() + damageAmountValue);
        company.setDamage_cost(newCompanyDamageCosts);
    }


    // getters & setters

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public void setHolderName(String holderName) {
        this.holderName = holderName;
    }

    public String getSurname(){
        return surname;
    }

    public String getNumber(){
        return number;
    }

    public LocalDate getExpirationDate(){
        return expirationDate;
    }

    public String getHolderName(){
        return holderName;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "surname='" + surname + '\'' +
                ", number='" + number + '\'' +
                ", expirationDate=" + expirationDate +
                ", holderName='" + holderName + '\'' +
                super.toString() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer customer)) return false;
        return  this.surname.equals(customer.getSurname()) && this.number.equals(customer.getNumber())
                && this.getName().equals(customer.getName())
                && this.getPassword().equals(customer.getPassword())
                && this.getCity().equals(customer.getCity())
                && this.getEmail().equals(customer.getEmail())
                && this.getPhone().equals(customer.getPhone())
                && this.getAFM().equals(customer.getAFM())
                && this.getZipcode().equals(customer.getZipcode())
                && this.expirationDate.equals(customer.getExpirationDate())
                && this.holderName.equals(customer.getHolderName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSurname(), getNumber(), getExpirationDate(), getHolderName());
    }
}