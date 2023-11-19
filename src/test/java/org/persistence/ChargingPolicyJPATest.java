package org.persistence;

import jakarta.persistence.EntityTransaction;
import jakarta.persistence.RollbackException;
import org.domain.ChargingPolicy;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


public class ChargingPolicyJPATest extends JPATest {

    @Test
    public void listChargingPolicies() {
        List<ChargingPolicy> policies = em.createQuery("select cp from ChargingPolicy cp").getResultList();
        assertEquals(2, policies.size());
    }

    @Test
    public void getMileageScaleOfFirstPolicy() {
        ChargingPolicy policy = (ChargingPolicy) em.createQuery("select cp from ChargingPolicy cp").getResultList().get(0);
        LinkedHashMap<Integer, Float> mileage_scale = new LinkedHashMap<Integer, Float>();
        mileage_scale.put(100, 0.10f);
        mileage_scale.put(200, 0.20f);
        mileage_scale.put(300, 0.30f);
        assertEquals(mileage_scale, policy.getMileageScale());

    }

}
