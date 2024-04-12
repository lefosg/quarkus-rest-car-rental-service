package org.infastructure.persistence;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.domain.company.ChargingPolicy;
import org.domain.company.ChargingPolicyRepository;
import org.infastructure.rest.representation.ChargingPolicyMapper;
import org.infastructure.rest.representation.ChargingPolicyRepresentation;
import org.jboss.resteasy.links.impl.NotFoundException;
import org.junit.jupiter.api.Test;
import org.util.DamageType;

import java.util.LinkedHashMap;

import static org.junit.jupiter.api.Assertions.*;
@QuarkusTest
public class ChargingPolicyRepositoryImplTest {

    @Inject
    ChargingPolicyRepository chargingPolicyRepository;

    @Inject
    ChargingPolicyMapper chargingPolicyMapper;

    @Test
    @Transactional
    void findByPolicyIdTest() {
        assertEquals(chargingPolicyRepository.findByPolicyId(1501).getId(),1501);
    }

    @Test
    @Transactional
    void findByPolicyInvalidIdTest() {
        assertNull(chargingPolicyRepository.findByPolicyId(1200));
    }


    private ChargingPolicyRepresentation createChargingPolicyRepresentation(Integer id) {
        LinkedHashMap<Integer, Float> mileageScale = new LinkedHashMap<>();
        mileageScale.put(100, 0.13f);
        mileageScale.put(200, 0.23f);
        mileageScale.put(300, 0.33f);
        LinkedHashMap<DamageType, Float> damages = new LinkedHashMap<>();
        damages.put(DamageType.NoDamage, 0f);
        damages.put(DamageType.Glasses, 100f);
        damages.put(DamageType.Machine, 300f);
        damages.put(DamageType.Interior, 250f);
        damages.put(DamageType.Tyres, 180f);
        damages.put(DamageType.Scratches, 80f);

        ChargingPolicyRepresentation representation = new ChargingPolicyRepresentation();
        representation.id = id;
        representation.mileageScale = mileageScale;
        representation.damageType = damages;
        return representation;
    }
}
