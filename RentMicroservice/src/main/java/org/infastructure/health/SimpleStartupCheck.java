package org.infastructure.health;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Startup;

@Startup
@ApplicationScoped
public class SimpleStartupCheck implements HealthCheck {

    @Override
    public HealthCheckResponse call() {
        if (isStarted()){
            return HealthCheckResponse.up("User Management Service started");
        }
        return HealthCheckResponse.down("User Management Service not started");
    }

    public boolean isStarted(){
        /* ... */
        return true;
    }

}
