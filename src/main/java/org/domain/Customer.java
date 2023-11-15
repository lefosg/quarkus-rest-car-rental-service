package org.domain;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@DiscriminatorValue("Customer")
public class Customer extends User{

    @Column(name="surname", length=30, nullable = false)
    private String surname;

    //credit card info

    @Column(name="number", length=12, nullable = false)
    private String number;

    @Column(name="expirationDate", length=30, nullable = false)
    private LocalDate expirationDate;

    @Column(name="holderName", length=25, nullable = false)
    private String holderName;

    public Customer(){
    }

    public Customer(String surname, String number, LocalDate expirationDate, String holderName,
                    String name, String password, String phone, String street, String city, String email, String zipcode){
        super(name, password, phone, street, city, email, zipcode);
        this.surname = surname;
        this.number = number;
        this.expirationDate = expirationDate;
        this.holderName = holderName;
    }

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
}