package org.persistence;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.NotFoundException;
import org.domain.Rent;

import java.util.List;

@RequestScoped
public class RentRepository implements PanacheRepositoryBase<Rent, Integer> {

    public void deleteAllRents() {
        List<Rent> rents = listAll();
        for (Rent r : rents) {
            delete(r);
        }
    }

    public void deleteRent(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("RentRepository: deleteRent\n\tid was null");
        }
        Rent rent = findById(id);
        if (rent == null) {
            throw new NotFoundException("RentRepository: deleteRent\n\trent found was null");
        }
        rent.setTechnicalCheck(null);
        delete(rent);
    }

    public List<Rent> findByMonth(int month) {
        if (month <= 0 || month > 12) {
            return listAll();
        }

        return find("select rent from Rent rent where month(rent.startDate) = :month",
                Parameters.with("month", month).map()).list();
    }
}
