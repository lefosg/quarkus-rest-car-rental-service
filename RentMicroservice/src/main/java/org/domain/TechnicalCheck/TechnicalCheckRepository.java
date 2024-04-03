package org.domain.TechnicalCheck;

import io.quarkus.panache.common.Parameters;
import jakarta.persistence.EntityManager;
import jakarta.ws.rs.NotFoundException;
import org.util.DamageType;

import java.util.List;
import java.util.Optional;

public interface TechnicalCheckRepository {
    public void deleteAllTechnicalChecks();

    public void deleteTechnicalCheck(Integer id);

    public List<TechnicalCheck> findByDamageType(DamageType damageType);

    public TechnicalCheck findTechnicalCheckById(Integer id);

    public Optional<TechnicalCheck> findTechnicalCheckByIdOptional(Integer id);

    List<TechnicalCheck> listAllTechnicalChecks();

    EntityManager getEntityManagerTechnicalCheck();

    void persistTechnicalCheck(TechnicalCheck tc);
}
