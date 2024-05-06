package org.infastructure.rest.resource;

import jakarta.enterprise.context.ApplicationScoped;

import jakarta.inject.Inject;
import org.application.FleetService;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.HealthCheckResponseBuilder;
import org.eclipse.microprofile.health.Readiness;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.infastructure.service.fleetManagament.FleetAPI;

@Readiness
@ApplicationScoped
public class FleetHealthReadiness implements HealthCheck {
    @Inject
    @RestClient
    FleetAPI fleetAPI;

    @Override
    public HealthCheckResponse call() {
        fleetAPI.getVehicles(2);
        return HealthCheckResponse.named("Fleet's Api").up().build();
    }
}
