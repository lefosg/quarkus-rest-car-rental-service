package org.domain;

import jakarta.persistence.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "CHARGING_POLICIES")
public class ChargingPolicy {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @ElementCollection
    private List<Integer> miles_scale;

    @ElementCollection
    private List<Float> cost_scale;

    //todo do sth with damage idk
    //@ElementCollection
    //private List<VehicleType> car_type;
//
    //@ElementCollection
    //private List<Integer> damage_cost_per_type;

    public ChargingPolicy() { }

    public ChargingPolicy(List<Integer> miles_scale, List<Float> cost_scale) {
        this.miles_scale = miles_scale;
        this.cost_scale = cost_scale;
    }

    public HashMap<Integer, Float> getMileageScale() {
        HashMap<Integer, Float> mileage_scale = new HashMap<Integer, Float>();
        for (int i=0; i < miles_scale.size(); i++) {
            mileage_scale.put(miles_scale.get(i), cost_scale.get(i));
        }
        return mileage_scale;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
