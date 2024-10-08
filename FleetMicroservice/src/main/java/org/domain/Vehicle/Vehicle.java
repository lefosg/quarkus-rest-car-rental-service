package org.domain.Vehicle;

import jakarta.persistence.*;
import org.util.Money;
import org.util.VehicleState;
import org.util.VehicleType;

import java.security.InvalidParameterException;
import java.util.Objects;

@Entity
@Table(name="VEHICLES")
public class Vehicle {

    @Id
    //@GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    protected Integer id;

    @Column(name="manufacturer",length =30, nullable = false)
    private String manufacturer;

    @Column(name="model",length =30, nullable = false)
    private String model;

    @Column(name="year_of_model", length=30, nullable = false)
    private int year;

    @Column(name="miles",length =30, nullable = false)
    private float miles;

    @Column(name="plate_number",length =30, nullable = false, unique = true)
    private String plateNumber;

    @Column(name="vehicle_type",length =30, nullable = false)
    @Enumerated(EnumType.STRING)
    private VehicleType vehicleType;

    @Column(name="vehicle_state",length =30, nullable = false)
    @Enumerated(EnumType.STRING)
    private VehicleState vehicleState;

    @Column(name="count_damages",length =30, nullable = false)
    private int countDamages;

    @Column(name="count_rents",length =30, nullable = false)
    private int countOfRents;

    @Column(name="fixed_charge",length = 30, nullable = false)
    private Money fixedCharge;

//    @ManyToOne(fetch=FetchType.LAZY)
//    //@OnDelete(action = OnDeleteAction.CASCADE)
//    //@Cascade(value = {org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
//    @JoinColumn(name="company_id")
//    private Company company;

    @Column(name="company_id", nullable = false)
    private Integer companyId;

    public Vehicle() {
        this.countDamages = 0;
        this.countOfRents=0;
        this.vehicleState = VehicleState.Available;
    }

    public Vehicle(String manufacturer, String model, int year, int miles, String plate_number, VehicleType vehicleType, Money fixed_cost){
        this.model = model;
        this.year = year;
        this.manufacturer = manufacturer;
        this.miles = miles;
        this.plateNumber = plate_number;
        this.vehicleType = vehicleType;
        this.fixedCharge = fixed_cost;
        this.vehicleState = VehicleState.Available;
        this.countDamages = 0;
        this.countOfRents=0;
    }

    // domain logic




    // getters & setters

    public int getCountOfRents() {
        return countOfRents;
    }

    public void setCountOfRents(int countOfRents) {
        this.countOfRents = countOfRents;
    }

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
        if (plate_number == null) {
            throw new NullPointerException();
        }
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

    public Money getFixedCharge() {
        return fixedCharge;
    }

    public void setFixedCharge(Money money) {
        if (money == null) {
            throw new NullPointerException();
        }
       // else if (money < 0) {
          //  throw new InvalidParameterException("Negative Input");
       // }
        this.fixedCharge = money;
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
        if (year < 0) {
            throw new InvalidParameterException("Invalid Input");
        }
       // else
       //     if (year ==  null{
        //    throw new NullPointerException();
      //  }
        this.year = year;
    }

    public float getMiles() {
        return miles;
    }

    public void setMiles(float miles) {
        if (miles< 0 ){
            throw new InvalidParameterException("Invalid Input");
        }
        this.miles = miles;
    }

    public int getCountDamages() {
        return countDamages;
    }

    public void setCountDamages(int count_damages) {
        this.countDamages = count_damages;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

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
                getVehicleType(), getVehicleState(), getDamageCount(), getFixedCharge(), getCompanyId());
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
                ", company=" + companyId +
                '}';
    }
}
