package org.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.util.DamageType;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

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
        damage_type.put(DamageType.Tyres,50f);
        damage_type.put(DamageType.Machine,30f);

        policy = new ChargingPolicy(mileage_scale,damage_type);
    }

    @Test
    public void calculatesMilesChargingCorrectly() {
        float cost = policy.calculateMileageCost(550);
        assertEquals(185f, cost);
    }

}