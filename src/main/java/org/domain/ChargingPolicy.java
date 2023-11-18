package org.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.*;

@Entity
@Table(name = "CHARGING_POLICIES")
public class ChargingPolicy {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @ElementCollection(fetch = FetchType.EAGER)  // map is small, make it eager
    @CollectionTable(name = "policy_mileage_scale", joinColumns = @JoinColumn(name = "policy_id"))
    @JoinColumn(name = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Cascade(value = org.hibernate.annotations.CascadeType.ALL)
    private Map<Integer, Float> mileageScale;


    //todo: do sth with damage idk
    //@ElementCollection
    //private List<VehicleType> car_type;

    //@ElementCollection
    //private List<Integer> damage_cost_per_type;

    public ChargingPolicy() { }


    public ChargingPolicy(LinkedHashMap<Integer, Float> mileage_scale) {
        this.mileageScale = mileage_scale;
    }

    // domain logic

    public float calculateMileageCost(float customer_miles) {
        float sum_cost = 0, sum_miles=0;
        int count=1;

        for (Map.Entry<Integer, Float> e : mileageScale.entrySet()) {

            Integer miles_scale = e.getKey();
            Float cost = e.getValue();
            if(count==mileageScale.size()){
                sum_cost+=(customer_miles-sum_miles)*cost;
                break;
            }
            else if (customer_miles > miles_scale) {
                sum_cost += (miles_scale - sum_miles) * cost;
                sum_miles = miles_scale;
                count+=1;
            } else if (customer_miles <= miles_scale) {
                sum_cost += (customer_miles - sum_miles) * cost;
                break;  // Break out of the loop since the customer_miles is within the current scale
            }
        }

        return sum_cost;
    }



    // getters & setters

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
