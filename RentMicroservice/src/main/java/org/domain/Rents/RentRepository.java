package org.domain.Rents;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.ws.rs.NotFoundException;

import java.util.List;
import java.util.Optional;


public interface RentRepository {

    void deleteAllRents();

    void deleteRent(Integer id);

    public List<Rent> findByMonth(int month);

    Rent findRentById(Integer id);

    Optional<Rent> findRentByIdOptional(Integer id);

    void persistRent(Rent rent);

    EntityManager getEntityManagerRent();

    List<Rent> listAllRents();

    Integer findMaxId();
}
