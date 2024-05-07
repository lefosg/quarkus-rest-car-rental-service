package org.util;

import org.domain.Vehicle.Vehicle;
import org.infastructure.rest.Representation.VehicleRepresentation;
import org.infastructure.service.user_management.representation.CompanyRepresentation;

public class Fixture {
    public static Vehicle vehicle7 = new Vehicle("OPEL", "CORSA", 2018, 64000, "PIP-4556", VehicleType.Hatchback, new Money(60));
    public static Vehicle vehicle8 = new Vehicle("AUDI", "A7", 2021, 100000, "MMA-8745", VehicleType.Sedan, new Money(70));
    public static Vehicle vehicle9 = new Vehicle("NISSAN", "QASHQAI AUTOMATIC", 2023, 50000, "ZIK-6834", VehicleType.SUV, new Money(100));
    public static Vehicle vehicle10 = new Vehicle("TOYOTA", "C-HR", 2022, 49000, "PAP-3333", VehicleType.SUV, new Money(80));
    public static Vehicle vehicle11 = new Vehicle("VOLKSWAGEN", "POLO", 2018, 73000, "NIK-9012", VehicleType.Hatchback, new Money(50));
}
