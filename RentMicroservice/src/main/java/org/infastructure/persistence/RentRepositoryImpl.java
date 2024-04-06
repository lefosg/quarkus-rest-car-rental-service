package org.infastructure.persistence;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.ws.rs.NotFoundException;
import org.domain.Rents.Rent;
import org.domain.Rents.RentRepository;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class RentRepositoryImpl implements PanacheRepositoryBase<Rent, Integer>, RentRepository {

    @Override
    public void deleteAllRents() {
        List<Rent> rents = listAll();
        for (Rent r : rents) {
            delete(r);
        }
    }

    @Override
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

    @Override
    public List<Rent> findByMonth(int month) {
        if (month <= 0 || month > 12) {
            return listAll();
        }

        return find("select rent from Rent rent where month(rent.startDate) = :month",
                Parameters.with("month", month).map()).list();
    }

    @Override
    public Rent findRentById(Integer id) {
        return findById(id);
    }

    @Override
    public Optional<Rent> findRentByIdOptional(Integer id) {
        return findByIdOptional(id);
    }

    @Override
    public void persistRent(Rent rent) {
        persist(rent);
    }

    @Override
    public EntityManager getEntityManagerRent() {
        return getEntityManager();
    }

    @Override
    public List<Rent> listAllRents() {
        return listAll();
    }

    //todo test this
    @Override
    public Integer findMaxId() {
        List<Rent> rents = listAllRents();
        int maxId = 0;
        for (Rent rent : rents) {
            if (rent.getId() > maxId) {
                maxId = rent.getId();
            }
        }
        return maxId;
    }
}
