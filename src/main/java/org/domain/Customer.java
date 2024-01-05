package org.domain;

import jakarta.persistence.*;
import jakarta.ws.rs.NotFoundException;
import org.util.Money;
import org.util.RentState;
import org.util.VehicleState;
import org.util.VehicleType;

import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.lang.Thread.sleep;

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

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy="customer")
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
    public List<Vehicle> viewAvailableVehicles(LocalDate startDate, LocalDate endDate) {
        //placeholders
        List<Vehicle> availableVehicles = new ArrayList<>();
        return availableVehicles;
    }

    /**
     * Creates a renting for a specific set of dates, and a specific vehicle.
     * @param startDate
     * @param endDate
     * @param vehicle
     */
    public void rent(LocalDate startDate, LocalDate endDate, Vehicle vehicle) {
        //input check
        if (startDate == null) {
            throw new NullPointerException("[!] Customer.rent: startDate is null");
        } else if (endDate == null) {
            throw new NullPointerException("[!] Customer.rent: endDate is null");
        } else if (vehicle == null) {
            throw new NullPointerException("[!] Customer.rent: vehicle is null");
        }
        if  (startDate.isAfter(endDate)) {
            throw new RuntimeException("Not good dates");
        }
        if (vehicle.getVehicleState() != VehicleState.Available) {
            throw new RuntimeException("The vehicle is not available");
        }
        //make the rent
        Rent rent1 = new Rent(startDate,endDate,vehicle,this);
        this.rents.add(rent1);
    }

    /**
     * Returns the vehicle to the company.
     * Charges for the customer are calculated.
     * Costs for possible damages are calculated (Vehicle state is set to Service temporarily).
     * Vehicle state is set back to Available.
     * The customer pays.
     * Rent state is set to Finished, Vehicle state is set to Available, and Vehicle miles are updated
     * @param vehicle
     * @param miles
     */
    public void returnVehicle(Vehicle vehicle, float miles) {
        //input check
        if (vehicle == null) {
            throw new NullPointerException("[!] Customer.returnVehicle: vehicle is null");
        }
        if (miles < 0) {
            throw new InvalidParameterException("[!] Customer.returnVehicle: miles is negative ("+miles+")");
        }

        Rent rent = null;
        //Customer may have done several rents, e.g. one 16/7-19/7 and one 20/7-22/7, but the day of the booking may be 10/7,
        //so all of them are still pending. Search to find for which rent the customer wants to return the vehicle
        for(int i=this.rents.size(); i > 0; i--) {
            if (this.rents.get(i-1).getRentedVehicle().equals(vehicle)) {
                rent = this.rents.get(i-1);
            }
        }
        //check if there is any rent ongoing with this vehicle
        if (rent == null) {
            throw new NotFoundException("[!] Customer.returnVehicle: rent searched for is null");
        }
        rent.calculateCosts(miles);
        Money amount = new Money(rent.getFixedCost().getAmount() + rent.getMileageCost().getAmount());
        pay(amount, rent.getDamageCost(), rent.getRentedVehicle().getCompany());
        //finalize rent
        rent.setRentState(RentState.Finished);
        rent.getRentedVehicle().setVehicleState(VehicleState.Available);
        rent.getRentedVehicle().setMiles(rent.getRentedVehicle().getMiles() + miles);
    }

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