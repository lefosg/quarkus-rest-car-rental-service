package org.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.Cascade;
import org.util.DamageType;
import org.util.Money;
import org.util.RentState;
import org.util.VehicleType;

import java.security.InvalidParameterException;
import java.time.LocalDate;
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
    private Money mileageCost;

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

    //cascade persists?
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id")
    private Vehicle rentedVehicle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private TechnicalCheck technicalCheck;


    public Rent() { }

    public Rent(LocalDate startDate, LocalDate endDate, Vehicle vehicle, Customer customer) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.rentedVehicle = vehicle;
        this.customer = customer;
        this.rentState = RentState.Pending;  //by default rent is initiated, meaning pending
    }

    // domain logic

    public void calculateDamageCost(VehicleType vehicleType, DamageType damageType) {
        this.damageCost = this.rentedVehicle.getCompany().calculateDamageCost(vehicleType, damageType);
    }

    public void calculateMileageCost(int miles) {
        this.mileageCost = this.rentedVehicle.getCompany().calculateMileageCost(miles);
    }

    public void calculateFixedCost() {
        this.fixedCost = this.rentedVehicle.getCompany().calculateFixedCharge(this.startDate, this.endDate, this.rentedVehicle.getId());
    }

    public void calculateTotalCost() {
        double total = fixedCost.getAmount() + mileageCost.getAmount() + damageCost.getAmount();
        totalCost = new Money(total);
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
        if (startDate.isBefore(LocalDate.now())){
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

    public void setTechnicalCheck(TechnicalCheck technicalCheck) {
        this.technicalCheck = technicalCheck;
    }

    @Override
    public String toString() {
        return "Rent{" +
                "id=" + id +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", rentState=" + rentState +
                ", fixedCost=" + fixedCost +
                ", mileageCost=" + mileageCost +
                ", damageCost=" + damageCost +
                ", totalCost=" + totalCost +
                ", rentedVehicle=" + rentedVehicle +
                ", customer=" + customer +
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
