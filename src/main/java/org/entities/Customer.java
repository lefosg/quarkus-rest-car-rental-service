package org.entities;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@DiscriminatorValue("Customer")
public class Customer extends User{


    @Column(name="surname", length=30, nullable = false)
    private String surname;

    @Column(name="number", length=12, nullable = false)
    private String number;

    @Column(name="expirationDate", length=30, nullable = false)
    private Date expirationDate;

    @Column(name="holderName", length=25, nullable = false)
    private String holderName;

    public Customer(){
    }

    public Customer(String surname, String number, Date expirationDate, String holderName,
                    String name, String phone, String street, String city, String email, String zipcode){
        super(name, phone, street, city, email, zipcode);
        this.surname = surname;
        this.number = number;
        this.expirationDate = expirationDate;
        this.holderName = holderName;
    }

    public String getSurname(){
        return surname;
    }
    
    public String getNumber(){
        return number;
    }

    public Date getExpirationDate(){
        return expirationDate;
    }

    public String getHolderName(){
        return holderName;
    }
}