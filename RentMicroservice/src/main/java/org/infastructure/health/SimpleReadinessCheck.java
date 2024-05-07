package org.infastructure.health;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Readiness;

@Readiness
@ApplicationScoped
public class SimpleReadinessCheck implements HealthCheck {

    @Override
    public HealthCheckResponse call() {
        if (isReady()) {
            return HealthCheckResponse.named("User Management Microservice UP").up().build();
        }
        return HealthCheckResponse.named("User Management Microservice DOWN").down().build();
    }

    private boolean isReady() {
        /* */
        return true;
    }
}
