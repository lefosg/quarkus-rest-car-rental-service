package org.infastructure.persistence;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.ws.rs.NotFoundException;

import org.domain.TechnicalCheck.TechnicalCheck;
import org.domain.TechnicalCheck.TechnicalCheckRepository;
import org.util.DamageType;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class TechnicalCheckRepositoryImpl implements PanacheRepositoryBase<TechnicalCheck,Integer>, TechnicalCheckRepository {

    @Override
    public void deleteAllTechnicalChecks() {
        List<TechnicalCheck> technicalChecks = listAll();
        for (TechnicalCheck t : technicalChecks) {
            t.setRent(null);
            delete(t);
        }
    }

    @Override
    public void deleteTechnicalCheck(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("TechnicalCheckRepository: deleteTechnicalCheck\n\tid was null");
        }
        TechnicalCheck technicalCheck = findById(id);
        if (technicalCheck == null) {
            throw new NotFoundException("TechnicalCheckRepository: deleteTechnicalCheck\n\ttechnical check found is null");
        }
        delete(technicalCheck);
    }

    @Override
    public List<TechnicalCheck> findByDamageType(DamageType damageType) {
        if (damageType == null)
            return listAll();

        return find("select technicalCheck from TechnicalCheck technicalCheck where technicalCheck.damageType = :damageType",
                Parameters.with("damageType", damageType).map()).list();
    }

    @Override
    public TechnicalCheck findTechnicalCheckById(Integer id) {
        return findById(id);
    }

    @Override
    public Optional<TechnicalCheck> findTechnicalCheckByIdOptional(Integer id) {
        return findByIdOptional(id);
    }

    @Override
    public List<TechnicalCheck> listAllTechnicalChecks() {
        return listAll();
    }

    @Override
    public EntityManager getEntityManagerTechnicalCheck() {
        return getEntityManager();
    }

    @Override
    public void persistTechnicalCheck(TechnicalCheck tc) {
        persist(tc);
    }

}
