package org.infastructure.persistence;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import org.domain.Vehicle.Vehicle;
import org.domain.Vehicle.VehicleRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;
import io.quarkus.security.identity.request.UsernamePasswordAuthenticationRequest;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.NotFoundException;
import org.util.VehicleState;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@ApplicationScoped
public class VehicleRepositoryImpl implements PanacheRepositoryBase<Vehicle,Integer>, VehicleRepository {

    @Override
    public List<Vehicle> listAllVehicles() {
        return listAll();
    }

    @Override
    public Vehicle findVehicleById(Integer id) {
        return findById(id);
    }

    @Override
    public Optional<Vehicle> findVehicleByIdOptional(Integer id) {
        return findByIdOptional(id);
    }

    @Override
    public void persistVehicle(Vehicle vehicle) {
        persist(vehicle);
    }

    @Override
    public EntityManager getVehicleEntityManager() {
        return getEntityManager();
    }

    @Override
    public void deleteAllVehicles() {
        List<Vehicle> vehicles = listAll();
        for (Vehicle v : vehicles) {
            //v.setCompany(null);
            delete(v);
        }
    }

    @Override
    public void deleteVehicle(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("VehicleRepository: deleteVehicle\n\tid was null");
        }
        Vehicle vehicle = findById(id);
        if (vehicle == null) {
            throw new NotFoundException("VehicleRepository: deleteVehicle\n\tvehicle found was null");
        }
        //vehicle.setCompany(null);
        delete(vehicle);
    }

    @Override
    public List<Vehicle> findByState(String state) {
        //if (state == null)
        //    return listAll();

        return find("select vehicle from Vehicle vehicle where vehicle.vehicleState = :vehicleState",
                Parameters.with("vehicleState", state).map()).list();
    }

    @Override
    public List<Vehicle> findByManufacturerAndState(String manufacturer, VehicleState vehicleState) {
        Map<String, Object> params = new HashMap<>();
        params.put("vehicleState", vehicleState);
        params.put("manufacturer", manufacturer);
        return find("select vehicle from Vehicle vehicle where vehicle.vehicleState = :vehicleState and vehicle.manufacturer = :manufacturer",
               params).list();
    }

    @Override
    public List<Vehicle> findByManufacturer(String manufacturer) {
        //if (manufacturer == null || manufacturer.equals(""))
        //    return listAll();

        return find("select vehicle from Vehicle vehicle where vehicle.manufacturer = :manufacturer",
                Parameters.with("manufacturer", manufacturer).map()).list();
    }

    @Override
    public List<Vehicle> findByModel(String model) {
        if (model == null)
            return listAll();

        return find("select vehicle from Vehicle vehicle where vehicle.model = :model",
                Parameters.with("model", model).map()).list();
    }

    @Override
    public List<Vehicle> findVehiclesByCompanyId(Integer companyId) {
        if (companyId == null)
            return listAll();

        return find("select vehicle from Vehicle vehicle where vehicle.companyId = :companyId",
                Parameters.with("companyId", companyId).map()).list();
    }
}
