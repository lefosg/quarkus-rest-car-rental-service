package org.infastructure.persistence;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import org.domain.company.ChargingPolicy;
import org.domain.company.ChargingPolicyRepository;

import java.util.List;
import java.util.Optional;

@RequestScoped
public class ChargingPolicyRepositoryImpl implements PanacheRepositoryBase<ChargingPolicy, Integer>, ChargingPolicyRepository {

    @Override
    public EntityManager getEntityManager() {
        return PanacheRepositoryBase.super.getEntityManager();
    }

    @Override
    public void persist(ChargingPolicy chargingPolicy) {
        PanacheRepositoryBase.super.persist(chargingPolicy);
    }

    @Override
    public ChargingPolicy findById(Integer integer) {
        return PanacheRepositoryBase.super.findById(integer);
    }

    @Override
    public Optional<ChargingPolicy> findByIdOptional(Integer integer) {
        return PanacheRepositoryBase.super.findByIdOptional(integer);
    }

    @Override
    public List<ChargingPolicy> listAll() {
        return PanacheRepositoryBase.super.listAll();
    }
}
