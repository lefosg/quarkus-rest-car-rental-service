package org.infastructure.health;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.application.FleetService;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Readiness;

@Readiness
@ApplicationScoped
public class SimpleReadinessCheck implements HealthCheck {

    @Inject
    FleetService fleetService;

    @Override
    public HealthCheckResponse call() {
        if (isReady()) {
            return HealthCheckResponse.named("Fleet Microservice UP").up().build();
        }
        return HealthCheckResponse.named("Fleet Microservice DOWN").down().build();
    }

    private boolean isReady() {
        return !fleetService.getFleet(2000).isEmpty();
    }
}
