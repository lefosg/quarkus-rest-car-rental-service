package org.domain.company;

import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

public interface ChargingPolicyRepository {

    EntityManager getPolicyEntityManager();

    void persistPolicy(ChargingPolicy chargingPolicy);

    ChargingPolicy findByPolicyId(Integer integer);

    Optional<ChargingPolicy> findByPolicyIdOptional(Integer integer);

    List<ChargingPolicy> listAllPolicies();

}
