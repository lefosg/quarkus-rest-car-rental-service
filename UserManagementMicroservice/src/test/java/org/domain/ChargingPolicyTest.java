package org.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.util.DamageType;

import java.util.LinkedHashMap;

import static org.junit.jupiter.api.Assertions.*;

class ChargingPolicyTest {

    ChargingPolicy policy;

    @BeforeEach
    public void setup() {
        LinkedHashMap<Integer, Float> mileage_scale = new LinkedHashMap<Integer, Float>();
        mileage_scale.put(100, 0.10f);
        mileage_scale.put(200, 0.20f);
        mileage_scale.put(300, 0.30f);
        mileage_scale.put(450, 0.5f);

        LinkedHashMap<DamageType, Float> damage_type = new LinkedHashMap<DamageType, Float>();
        damage_type.put(DamageType.Glasses,50f);
        damage_type.put(DamageType.Machine,70f);
        damage_type.put(DamageType.Tyres,60f);
        damage_type.put(DamageType.Scratches,10f);
        damage_type.put(DamageType.Interior,20f);
        damage_type.put(DamageType.NoDamage,0f);

        policy = new ChargingPolicy(mileage_scale,damage_type);
    }

    @Test
    public void calculatesMilesChargingCorrectly() {
        float cost = policy.calculateMileageCost(550);
        assertEquals(185f, cost);
    }

    @Test
    public void calculateDamageCost() {
        assertEquals(50, policy.calculateDamageCost(DamageType.Glasses));
    }

}