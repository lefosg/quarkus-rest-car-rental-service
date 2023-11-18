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
    protected Integer Id;

    @Column(name="model",length =30, nullable = false)
    private String model;
    @Column(name="year", length=30, nullable = false)
    private int year;
    @Column(name="manufacturer",length =30, nullable = false)
    private String manufacturer;
    @Column(name="plate_number",length =30, nullable = false)
    private String plate_number;
//    @Column(name="vehicle_type",length =30, nullable = false)
//    private VehicleType vehicleType;
//    @Column(name="vehicle_state",length =30, nullable = false)
//    private VehicleState vehicleState;
//    @Column(name="count_damages",length =30, nullable = false)
//    private int count;
//    @Column(name="fixed_charge",length = 30, nullable = false)
//    private int money;

    //@ManyToOne(fetch=FetchType.LAZY)
    //@JoinColumn(name="company_id")
    //private Company company;

    public Vehicle(){}
    public Vehicle(String model,int year,String manufacturer,String plate_number){
        this.model=model;
        this.year=year;
        this.manufacturer=manufacturer;
        this.plate_number=plate_number;
//        this.vehicleType=vehicleType;
//        this.vehicleState=vehicleState;
//        this.count=count;
//        this.money=money;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getPlate_number() {
        return plate_number;
    }

    public void setPlate_number(String plate_number) {
        this.plate_number = plate_number;
    }

//    public VehicleType getVehicleType() {
//        return vehicleType;
//    }
//
//    public void setVehicleType(VehicleType vehicleType) {
//        this.vehicleType = vehicleType;
//    }
//
//    public VehicleState getVehicleState() {
//        return vehicleState;
//    }
//
//    public void setVehicleState(VehicleState vehicleState) {
//        this.vehicleState = vehicleState;
//    }

//    public int getCount() {
//        return count;
//    }
//
//    public void setCount(int count) {
//        this.count = count;
//    }
//
//    public int getMoney() {
//        return money;
//    }

//    public void setMoney(int money) {
//        this.money = money;
//    }

//    public Company getCompany() {
//        return company;
//    }





    public String getModel() {
        return model;
    }


    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

//    public void setCompany(Company company) {
//        this.company = company;
//    }

    public void setModel(String model) {
        this.model = model;
    }
}
