package org.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.util.DamageType;

import java.util.*;

@Entity
@Table(name = "CHARGING_POLICIES")
public class ChargingPolicy {

    @Id
    //@GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "policy_mileage_scale", joinColumns = @JoinColumn(name = "policy_id"))
    @JoinColumn(name = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Cascade(value = org.hibernate.annotations.CascadeType.ALL)
    private Map<Integer, Float> mileageScale;


    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "policy_damage_cost", joinColumns = @JoinColumn(name = "policy_id"))
    @MapKeyEnumerated(EnumType.STRING)
    @JoinColumn(name = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Cascade(value = org.hibernate.annotations.CascadeType.ALL)
    private Map<DamageType, Float> damageType;

    public ChargingPolicy() { }

    public ChargingPolicy(LinkedHashMap<Integer, Float> mileage_scale, LinkedHashMap<DamageType, Float> damage_type) {
        this.mileageScale = mileage_scale;
        this.damageType=damage_type;
    }

    // domain logic

    public float calculateMileageCost(float customer_miles) {
        if (customer_miles < 0 ) {
            throw new IllegalArgumentException("[!] ChargingPolicy.calculateMileageCost\n\tnegative number in miles parameter");
        }
        if (customer_miles == 0) {
            return 0;
        }
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

    public float calculateDamageCost(DamageType damageType) {
        return this.damageType.get(damageType);
    }


    // getters & setters
    public Map<DamageType, Float> getDamageType() {
        return damageType;
    }

    public void setDamageType(Map<DamageType, Float> damageType) {
        this.damageType = damageType;
    }
    public Map<Integer, Float> getMileageScale() {
        return mileageScale;
    }

    public void setMileageScale(Map<Integer, Float> mileage_scale) {
        this.mileageScale = mileage_scale;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this==o) return  true;
        if (!(o instanceof ChargingPolicy policy)) return false;
        return Objects.equals(this.getMileageScale(), policy.getMileageScale()) &&
                Objects.equals(this.getDamageType(), policy.getDamageType());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMileageScale(), getDamageType());
    }

    @Override
    public String toString() {
        return "ChargingPolicy{" +
                "mileageScale=" + mileageScale +
                ", damageType=" + damageType +
                '}';
    }
}
