package org.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.util.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@Entity
@Table(name = "RENTS")
public class Rent {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    protected Integer id;

    @Column(name = "startDate")
    private LocalDate startDate;

    @Column(name = "endDate")
    private LocalDate endDate;

    @Column(name = "rentState")
    @Enumerated(EnumType.STRING)
    private RentState rentState;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="amount",column=@Column(name="fixedCost_amount")),
            @AttributeOverride(name="currency",column=@Column(name="fixedCost_currency")),
    })
    private Money fixedCost;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="amount",column=@Column(name="mileageCost_amount")),
            @AttributeOverride(name="currency",column=@Column(name="mileageCost_currency")),
    })
    //@Transient
    private Money mileageCost;

    @Column(name="miles")
    private float miles;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="amount",column=@Column(name="damageCost_amount")),
            @AttributeOverride(name="currency",column=@Column(name="damageCost_currency")),
    })
    private Money damageCost;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="amount",column=@Column(name="totalCost_amount")),
            @AttributeOverride(name="currency",column=@Column(name="totalCost_currency")),
    })
    private Money totalCost;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "vehicle_id")
    private Vehicle rentedVehicle;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REMOVE}, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name="technical_check_id")
    private TechnicalCheck technicalCheck;


    public Rent() {
        //this.technicalCheck = new TechnicalCheckImpl(this);
    }

    public Rent(LocalDate startDate, LocalDate endDate, Vehicle vehicle, Customer customer) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.rentedVehicle = vehicle;
        this.customer = customer;
        this.rentState = RentState.Pending;  //by default rent is initiated to pending state
        this.rentedVehicle.setVehicleState(VehicleState.Rented);
        this.technicalCheck = new TechnicalCheckImpl(this);
    }

    // domain logic

    private void calculateDamageCost() {
        DamageType damageType = technicalCheck.checkForDamage();
        this.damageCost = this.rentedVehicle.getCompany().calculateDamageCost(damageType);
    }

    private void calculateMileageCost(float miles) {
        this.miles = miles;
        this.mileageCost = this.rentedVehicle.getCompany().calculateMileageCost(miles);
    }

    private void calculateFixedCost() {
        this.fixedCost = this.rentedVehicle.getCompany().calculateFixedCharge(this.startDate, this.endDate, this.rentedVehicle.getFixedCharge());
    }

    private void calculateTotalCost() {
        double total = fixedCost.getAmount() + mileageCost.getAmount() + damageCost.getAmount();
        totalCost = new Money(total);
    }

    public void calculateCosts(float miles) {
        this.calculateMileageCost(miles);
        this.calculateDamageCost();
        this.calculateFixedCost();
        this.calculateTotalCost();
    }

    public int getDurationInDays() {
        return (int) this.startDate.until(this.endDate, ChronoUnit.DAYS)+1;
    }

    // getters & setters


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        if (startDate.isAfter(LocalDate.now())){
            throw new RuntimeException("Έχετε δώσει προγενέστερη ημερομηνία έναρξης ενοικίασης");
        }
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public RentState getRentState() {
        return rentState;
    }

    public void setRentState(RentState rentState) {
        this.rentState = rentState;
    }

    public Money getFixedCost() {
        return fixedCost;
    }

    public void setFixedCost(Money fixedCost) {
        this.fixedCost = fixedCost;
    }

    public Money getMileageCost() {
        return mileageCost;
    }

    public void setMileageCost(Money mileageCost) {
        this.mileageCost = mileageCost;
    }

    public Money getDamageCost() {
        return damageCost;
    }

    public void setDamageCost(Money damageCost) {
        this.damageCost = damageCost;
    }

    public Money getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Money totalCost) {
        this.totalCost = totalCost;
    }

    public Vehicle getRentedVehicle() {
        return rentedVehicle;
    }

    public void setRentedVehicle(Vehicle rentedVehicle) {
        this.rentedVehicle = rentedVehicle;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public TechnicalCheck getTechnicalCheck() {
        return technicalCheck;
    }

    public void setTechnicalCheck(TechnicalCheck technicalCheckImpl) {
        this.technicalCheck = technicalCheckImpl;
    }

    public float getMiles() {
        return miles;
    }

    public void setMiles(float miles) {
        this.miles = miles;
        //calculateMileageCost(miles);
    }

    @Override
    public String toString() {
        return "Rent{" +
                "id=" + id +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", rentState=" + rentState +
                ", fixedCost=" + fixedCost +
                ", miles=" + miles +
                ", mileageCost=" + mileageCost +
                ", damageCost=" + damageCost +
                ", totalCost=" + totalCost +
                ", rentedVehicle=" + rentedVehicle +
                ", customer=" + customer +
                ", technicalCheck=" + technicalCheck +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Rent rent)) return false;
        return Objects.equals(getStartDate(), rent.getStartDate())
                && Objects.equals(getEndDate(), rent.getEndDate())
                && getRentState() == rent.getRentState()
                && Objects.equals(getFixedCost(), rent.getFixedCost())
                && Objects.equals(getMileageCost(), rent.getMileageCost())
                && Objects.equals(getDamageCost(), rent.getDamageCost())
                && Objects.equals(getTotalCost(), rent.getTotalCost())
                && Objects.equals(getRentedVehicle(), rent.getRentedVehicle())
                && Objects.equals(getCustomer(), rent.getCustomer());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStartDate(), getEndDate(), getRentState(), getFixedCost(), getMileageCost(),
                getDamageCost(), getTotalCost(), getRentedVehicle(), getCustomer());
    }
}
