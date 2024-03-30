package org.domain.company;

import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

public interface ChargingPolicyRepository {

    EntityManager getEntityManager();

    void persist(ChargingPolicy chargingPolicy);

    ChargingPolicy findById(Integer integer);

    List<ChargingPolicy> listAll();

    Optional<ChargingPolicy> findByIdOptional(Integer integer);
}
