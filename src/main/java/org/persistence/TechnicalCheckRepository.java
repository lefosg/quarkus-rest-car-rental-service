package org.persistence;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;
import io.quarkus.security.identity.request.UsernamePasswordAuthenticationRequest;
import jakarta.enterprise.context.RequestScoped;
import org.domain.ChargingPolicy;
import org.domain.Company;
import org.domain.Customer;
import org.domain.TechnicalCheck;
import org.util.DamageType;

import java.util.List;

@RequestScoped
public class TechnicalCheckRepository implements PanacheRepositoryBase<TechnicalCheck,Integer>{

    public List<TechnicalCheck> findByDamageType(DamageType damageType) {
        if (damageType == null)
            return listAll();

        return find("select technicalCheck from TechnicalCheck technicalCheck where technicalCheck.damageType = :damageType",
                Parameters.with("damageType", damageType).map()).list();
    }



}
