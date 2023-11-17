package org.domain;

import jakarta.persistence.*;
import java.util.HashMap;

@Entity
@Table(name = "CHARGING_POLICIES")
public class ChargingPolicy {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "mileage_scale", nullable = false)
    private HashMap<Integer, Float> mileageScale = new HashMap<Integer, Float>();
//
    @Column(name = "vehicle_to_int", nullable = false)
    private HashMap<VehicleType, Integer> vehicle_to_int= new HashMap<VehicleType, Integer>();
//
    public ChargingPolicy() { }

    ChargingPolicy(HashMap<Integer, Float> mileageScale, HashMap<VehicleType, Integer> vehicle_to_int) {
        this.mileageScale = mileageScale;
        this.vehicle_to_int = vehicle_to_int;
    }

    public HashMap getMileageScale(){
        return this.mileageScale;
    }

    public HashMap getVehicleToInt(){
        return this.vehicle_to_int;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
