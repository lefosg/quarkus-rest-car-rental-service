package org.persistence;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.RequestScoped;
import org.domain.Company;

import java.util.List;

@RequestScoped
public class CompanyRepository implements PanacheRepositoryBase<Company, Integer> {

    public List<Company> findByCity(String city) {
        if (city == null || city.equals(""))
            return listAll();

        return find("select company from Company company where company.city = :city",
                Parameters.with("city", city).map()).list();
    }
}

