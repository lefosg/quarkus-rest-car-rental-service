package org.persistence;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;
import io.quarkus.security.identity.request.UsernamePasswordAuthenticationRequest;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.NotFoundException;
import org.domain.Company;
import org.domain.Customer;
import org.domain.Vehicle;
import org.util.VehicleState;
import org.util.VehicleType;

import java.util.List;

@RequestScoped
public class VehicleRepository implements PanacheRepositoryBase<Vehicle,Integer>{

    public void deleteAllVehicles() {
        List<Vehicle> vehicles = listAll();
        for (Vehicle v : vehicles) {
            v.setCompany(null);
            delete(v);
        }
    }

    public void deleteVehicle(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("VehicleRepository: deleteVehicle\n\tid was null");
        }
        Vehicle vehicle = findById(id);
        if (vehicle == null) {
            throw new NotFoundException("VehicleRepository: deleteVehicle\n\tvehicle found was null");
        }
        vehicle.setCompany(null);
        delete(vehicle);
    }

    public List<Vehicle> findByModel(String model) {
        if (model == null)
            return listAll();

        return find("select vehicle from Vehicle vehicle where vehicle.model = :model",
                Parameters.with("model", model).map()).list();
    }


    public List<Vehicle> findByManufacturer(String manufacturer) {
        if (manufacturer == null || manufacturer.equals(""))
            return listAll();

        return find("select vehicle from Vehicle vehicle where vehicle.manufacturer = :manufacturer",
                Parameters.with("manufacturer", manufacturer).map()).list();
    }

    public List<Vehicle> findByYear(Integer year) {
        if (year == null)
            return listAll();

        return find("select vehicle from Vehicle vehicle where vehicle.year = :year",
                Parameters.with("year", year).map()).list();
    }

    public List<Vehicle> findByMiles(Float miles) {
        if (miles == null)
            return listAll();

        return find("select vehicle from Vehicle vehicle where vehicle.miles = :miles",
                Parameters.with("miles",miles).map()).list();
    }

    public List<Vehicle> findByplateNumber(String plateNumber) {
        if (plateNumber == null)
            return listAll();

        return find("select vehicle from Vehicle vehicle where vehicle.plateNumber = :plateNumber",
                Parameters.with("plateNumber",plateNumber).map()).list();
    }
    public List<Vehicle> findByvehicleState(VehicleState vehicleState) {
        if (vehicleState == null)
            return listAll();

        return find("select vehicle from Vehicle vehicle where vehicle.vehicleState = :vehicleState",
                Parameters.with("vehicleState",vehicleState).map()).list();
    }
    public List<Vehicle> findByvehicleType(VehicleType vehicleType) {
        if (vehicleType == null)
            return listAll();

        return find("select vehicle from Vehicle vehicle where vehicle.vehicleType = :vehicleType",
                Parameters.with("vehicleType",vehicleType).map()).list();
    }


}