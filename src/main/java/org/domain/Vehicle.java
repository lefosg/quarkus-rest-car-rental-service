package org.domain;

import jakarta.persistence.*;
import java.util.Objects;
import org.util.Money;
import org.util.VehicleState;
import org.util.VehicleType;

@Entity
@Table(name="VEHICLES")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    protected Integer id;

    @Column(name="manufacturer",length =30, nullable = false)
    private String manufacturer;

    @Column(name="model",length =30, nullable = false)
    private String model;

    @Column(name="year_of_model", length=30, nullable = false)
    private int year;

    @Column(name="miles",length =30, nullable = false)
    private int miles;

    @Column(name="plate_number",length =30, nullable = false)
    private String plateNumber;

    @Column(name="vehicle_type",length =30, nullable = false, unique = true)
    private VehicleType vehicleType;

    @Column(name="vehicle_state",length =30, nullable = false)
    private VehicleState vehicleState;

    @Column(name="count_damages",length =30, nullable = false)
    private int countDamages;

    @Column(name="fixed_charge",length = 30, nullable = false)
    private Money fixedCharge;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="company_id")
    private Company company;

    public Vehicle() { }

    public Vehicle(String manufacturer, String model, int year, int miles, String plate_number, VehicleType vehicleType, Money fixed_cost){
        this.model=model;
        this.year=year;
        this.manufacturer=manufacturer;
        this.miles = miles;
        this.plateNumber =plate_number;
        this.vehicleType=vehicleType;
        this.fixedCharge =fixed_cost;
        this.vehicleState=VehicleState.Available;
        this.countDamages =0;
    }

    // domain logic




    // getters & setters

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plate_number) {
        this.plateNumber = plate_number;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public VehicleState getVehicleState() {
        return vehicleState;
    }

    public void setVehicleState(VehicleState vehicleState) {
        this.vehicleState = vehicleState;
    }

    public int getDamageCount() {
        return countDamages;
    }

    public void setDamageCount(int count) {
        this.countDamages = count;
    }

    public Money getFixedCharge() {
        return fixedCharge;
    }

    public void setFixedCharge(Money money) {
        this.fixedCharge = money;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMiles() {
        return miles;
    }

    public void setMiles(int miles) {
        this.miles = miles;
    }

    public int getCountDamages() {
        return countDamages;
    }

    public void setCountDamages(int count_damages) {
        this.countDamages = count_damages;
    }

    //todo: define vehicle equality??
    /**
     * Checks vehicle equality based on year, miles, manufacturer,
     * model, plate number, and vehicle type.
     * It does not consider if they belong to the same company or not,
     * their fixed cost, miles, availability, and count of damages
     * @param o - a vehicle to compare
     * @returns true if they are the same, false if not
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vehicle vehicle)) return false;
        return  getYear() == vehicle.getYear()
                && Objects.equals(getManufacturer(), vehicle.getManufacturer())
                && Objects.equals(getModel(), vehicle.getModel())
                && Objects.equals(getPlateNumber(), vehicle.getPlateNumber())
                && getVehicleType() == vehicle.getVehicleType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getManufacturer(), getModel(), getYear(), getMiles(), getPlateNumber(),
                getVehicleType(), getVehicleState(), getDamageCount(), getFixedCharge(), getCompany());
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "id=" + id +
                ", manufacturer='" + manufacturer + '\'' +
                ", model='" + model + '\'' +
                ", year=" + year +
                ", miles=" + miles +
                ", plate_number='" + plateNumber + '\'' +
                ", vehicleType=" + vehicleType +
                ", vehicleState=" + vehicleState +
                ", count=" + countDamages +
                ", money=" + fixedCharge +
                ", company=" + company +
                '}';
    }
}
