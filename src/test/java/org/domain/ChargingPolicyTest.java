package org.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        policy = new ChargingPolicy(mileage_scale);
    }

    @Test
    public void calculatesMilesChargingCorrectly() {
        float cost = policy.calculateMileageCost(150);
        assertEquals(20f, cost);
    }

}