package org.infastructure.health;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.domain.Vehicle.VehicleRepository;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Readiness;

@Readiness
@ApplicationScoped
public class SimpleReadinessCheck implements HealthCheck {

    @Inject
    VehicleRepository vehicleRepository;

    @Override
    public HealthCheckResponse call() {
        if (isReady()) {
            return HealthCheckResponse.named("Fleet Microservice UP").up().build();
        }
        return HealthCheckResponse.named("Fleet Microservice DOWN").down().build();
    }

    private boolean isReady() {
        if (vehicleRepository.listAllVehicles().isEmpty())
            return false;
        return true;
    }
}
