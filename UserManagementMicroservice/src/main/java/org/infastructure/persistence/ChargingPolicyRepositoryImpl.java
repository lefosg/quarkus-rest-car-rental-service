package org.infastructure.persistence;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import org.domain.company.ChargingPolicy;
import org.domain.company.ChargingPolicyRepository;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ChargingPolicyRepositoryImpl implements PanacheRepositoryBase<ChargingPolicy, Integer>, ChargingPolicyRepository {

    @Override
    public EntityManager getPolicyEntityManager() {
        return getEntityManager();
    }

    @Override
    public void persistPolicy(ChargingPolicy chargingPolicy) {
        persist(chargingPolicy);
    }

    @Override
    public ChargingPolicy findByPolicyId(Integer integer) {
        return findById(integer);
    }
    @Override
    public Optional<ChargingPolicy> findByPolicyIdOptional(Integer integer) {
        return findByIdOptional(integer);
    }

    @Override
    public List<ChargingPolicy> listAllPolicies() {
        return listAll();
    }
}
