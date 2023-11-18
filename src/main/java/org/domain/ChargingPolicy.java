package org.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;
import java.util.Map;

@Entity
@Table(name = "CHARGING_POLICIES")
public class ChargingPolicy {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "policy_mileage_scale", joinColumns = @JoinColumn(name = "policy_id"))
    @MapKeyColumn(name = "fault_category_key")
    @Column(name = "mileage_scale")     // choose the name of the DB column used to store the Map<> value
    @JoinColumn(name = "id")            // name of the @Id column of this entity
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Cascade(value = org.hibernate.annotations.CascadeType.ALL)
    private Map<Integer, Float> mileageScale;

    //@ElementCollection(fetch = FetchType.EAGER)
    //@Column(name = "miles_scale")
    //private List<Integer> miles_scale;
//
    //@ElementCollection(fetch = FetchType.EAGER)
    //@Column(name = "cost_scale")
    //private List<Float> cost_scale;

    //todo: do sth with damage idk
    //@ElementCollection
    //private List<VehicleType> car_type;
//
    //@ElementCollection
    //private List<Integer> damage_cost_per_type;

    public ChargingPolicy() { }

    public ChargingPolicy(List<Integer> miles_scale, List<Float> cost_scale) {
        //this.miles_scale = miles_scale;
        //this.cost_scale = cost_scale;
    }

    public ChargingPolicy(Map<Integer, Float> mileage_scale) {
        this.mileageScale = mileage_scale;
    }

    public Map<Integer, Float> getMileageScale() {
        return mileageScale;
    }

    public void setMileageScale(Map<Integer, Float> mileage_scale) {
        this.mileageScale = mileage_scale;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
