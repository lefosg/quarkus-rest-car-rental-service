package org.persistence;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;
import io.quarkus.security.identity.request.UsernamePasswordAuthenticationRequest;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.NotFoundException;
import org.domain.ChargingPolicy;
import org.domain.Company;
import org.domain.Customer;
import org.domain.TechnicalCheck;
import org.util.DamageType;

import java.util.List;

@RequestScoped
public class TechnicalCheckRepository implements PanacheRepositoryBase<TechnicalCheck,Integer>{

    public void deleteAllTechnicalChecks() {
        List<TechnicalCheck> technicalChecks = listAll();
        for (TechnicalCheck t : technicalChecks) {
            t.setRent(null);
            delete(t);
        }
    }

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

    public List<TechnicalCheck> findByDamageType(DamageType damageType) {
        if (damageType == null)
            return listAll();

        return find("select technicalCheck from TechnicalCheck technicalCheck where technicalCheck.damageType = :damageType",
                Parameters.with("damageType", damageType).map()).list();
    }



}
