package org.infastructure.rest.Resource;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Readiness;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.infastructure.service.user_management.UserManagementApi;

@Readiness
@ApplicationScoped
public class CompanyHealthReadiness implements HealthCheck {
    @Inject
    @RestClient
    UserManagementApi userManagementApi;

    @Override
    public HealthCheckResponse call() {
        try{
            userManagementApi.getCompany(2000);
            return HealthCheckResponse.named("Company's Api").up().build();}
        catch(Exception e){
            return HealthCheckResponse.named("Company's Api is DOWN").down().build();
        }
    }
}
