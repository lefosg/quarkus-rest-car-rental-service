package org.domain.Vehicle;

import jakarta.persistence.EntityManager;
import org.util.VehicleState;
import java.util.List;
import java.util.Optional;

public interface VehicleRepository {

    public List<Vehicle> listAllVehicles();

    Vehicle findVehicleById(Integer id);

    Optional<Vehicle> findVehicleByIdOptional(Integer id);

    void persistVehicle(Vehicle vehicle);

    public EntityManager getVehicleEntityManager();

    void deleteAllVehicles();

    void deleteVehicle(Integer id);

    List<Vehicle> findByModel(String model);

    List<Vehicle> findByManufacturer(String manufacturer);

    List<Vehicle> findByState(String state);

}
