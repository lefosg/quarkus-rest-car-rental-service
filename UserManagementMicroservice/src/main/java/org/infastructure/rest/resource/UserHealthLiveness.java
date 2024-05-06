package org.infastructure.rest.resource;

import jakarta.inject.Inject;
import org.application.FleetService;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@Liveness
public class UserHealthLiveness implements HealthCheck {
 // This Healthcheck show that the app is up and running
    @Override
    public HealthCheckResponse call() {
        return HealthCheckResponse.up("Simple health check");
    }
}
