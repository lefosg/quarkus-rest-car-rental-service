package org.persistence;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;
import io.quarkus.security.identity.request.UsernamePasswordAuthenticationRequest;
import jakarta.enterprise.context.RequestScoped;
import org.domain.Company;
import org.domain.Customer;
import org.domain.Rent;

import java.util.List;

@RequestScoped
public class RentRepository implements PanacheRepositoryBase<Rent, Integer>{

    public List<Rent> findByMonth(int month) {
        if (month <= 0 || month > 12) {
            return listAll();
        }

        return find("select rent from Rent rent where month(rent.startDate) = :month",
                Parameters.with("month", month).map()).list();
    }
}
