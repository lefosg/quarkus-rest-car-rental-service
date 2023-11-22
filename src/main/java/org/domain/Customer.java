package org.domain;

import jakarta.persistence.*;
import org.util.Money;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@DiscriminatorValue("Customer")
public class Customer extends User{

    @Column(name="surname", length=30)
    private String surname;

    //credit card info
    @Column(name="number", length=16)
    private String number;

    @Column(name="expirationDate", length=30)
    private LocalDate expirationDate;

    @Column(name="holderName", length=25)
    private String holderName;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy="customer")
    private List<Rent> rents = new ArrayList<>();

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
     * Returns a list of vehicles available for renting the dates that the user gave as input
     * @param startDate
     * @param endDate
     * @return a list of available vehicles
     */
    public List<Vehicle> viewAvailableCars(LocalDate startDate, LocalDate endDate) {
        //placeholder
        return new ArrayList<>();
    }

    /**
     * Creates a renting for a specific set of dates, and a specific vehicle.
     * @param startDate
     * @param endDate
     * @param vehicle
     */
    public void rent(LocalDate startDate, LocalDate endDate, Vehicle vehicle) {

    }

    /**
     * Finalizes the rent procedure
     */
    public void returnVehicle() {

    }


    /**
     * Increases the <i>Company.income</i> (and depending on the case, <i>Company.damage_cost</i>) by <i>amount</i>.
     * @param amount
     */
    public void pay(Money amount) {

    }


    @Override
    public void dashboard() {

    }

    public void addRent(Rent rent) {
        this.rents.add(rent);
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

    public List<Rent> getRents() {
        return rents;
    }

    public void setRents(List<Rent> rents) {
        this.rents = rents;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "surname='" + surname + '\'' +
                ", number='" + number + '\'' +
                ", expirationDate=" + expirationDate +
                ", holderName='" + holderName + '\'' +
                ", id=" + id +
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