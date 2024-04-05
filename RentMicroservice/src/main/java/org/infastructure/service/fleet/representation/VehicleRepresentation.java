package org.infastructure.service.fleet.representation;

import io.quarkus.runtime.annotations.RegisterForReflection;


@RegisterForReflection
public class VehicleRepresentation {
    public Integer id;

    public Integer getId() {
        return id;
    }
}