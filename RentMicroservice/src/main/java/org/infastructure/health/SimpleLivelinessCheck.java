package org.infastructure.health;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;

@Liveness
@ApplicationScoped
public class SimpleLivelinessCheck implements HealthCheck {
    @Override
    public HealthCheckResponse call() {
        if (isHealthy()){
            return HealthCheckResponse.up("User Management Service is HEALTHY");
        }
        return HealthCheckResponse.down("User Management Service is FAILING");
    }

    public boolean isHealthy(){
        /* ... */
        return true;
    }
}