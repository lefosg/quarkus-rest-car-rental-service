package org.persistence;

import org.domain.ChargingPolicy;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class ChargingPolicyJPATest extends JPATest {

    //@Test
    public void listChargingPolicies() {
        List<ChargingPolicy> policies = em.createQuery("select cp from CharingPolicy cp").getResultList();
        assertEquals(0, policies.size());
    }

}
