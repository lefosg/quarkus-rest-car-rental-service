package org.infastructure.service.fleet.representation;

import io.quarkus.runtime.annotations.RegisterForReflection;
import org.util.Money;
import org.util.VehicleState;
import org.util.VehicleType;


@RegisterForReflection
public class VehicleRepresentation {
    public Integer id;
    public String manufacturer;
    public String model;
    public int year;
    public float miles;
    public String plateNumber;
    public VehicleType vehicleType;
    public VehicleState vehicleState;
    public Money fixedCharge;
    public Integer companyId;
    public Integer countDamages;
    public Integer countOfRents;
}