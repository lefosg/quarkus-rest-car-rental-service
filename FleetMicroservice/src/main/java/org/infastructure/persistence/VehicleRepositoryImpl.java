package org.infastructure.persistence;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import org.domain.Vehicle.Vehicle;
import org.domain.Vehicle.VehicleRepository;

public class VehicleRepositoryImpl implements PanacheRepositoryBase<Vehicle,Integer>, VehicleRepository {
}
