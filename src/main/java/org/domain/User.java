package org.domain;

import jakarta.persistence.*;


@Entity
@Table(name="USERS")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(
    name="type",
    discriminatorType=DiscriminatorType.STRING
)
public abstract class User{

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Integer id;

    @Column(name="name", length=30, nullable = false)
    private String name;

    @Column(name="email", length=25, nullable = false)
    private String email;

    @Column(name="password", length=30, nullable = false)
    private String password;

    @Column(name="phone", length=12, nullable = false)
    private String phone;

    @Column(name="street", length=30, nullable = false)
    private String street;

    @Column(name="city", length=25, nullable = false)
    private String city;

    @Column(name="zipcode", length=5, nullable = false)
    private String zipcode;

    public User(){
    }

    public User(String name, String email, String password, String phone, String street, String city, String zipcode){
        this.name=name;
        this.email=email;
        this.password = password;
        this.phone=phone;
        this.street=street;
        this.city=city;
        this.zipcode=zipcode;

    }

    public String getName(){
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone(){
        return phone;
    }

    public String getStreet(){
        return street;
    }

    public String getCity(){
        return city;
    }

    public String getEmail(){
        return email;
    }

    public String getZipcode(){
        return zipcode;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }


}