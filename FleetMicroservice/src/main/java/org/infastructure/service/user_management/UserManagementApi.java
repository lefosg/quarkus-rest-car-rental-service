package org.infastructure.service.user_management;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.infastructure.service.user_management.representation.CompanyRepresentation;
import org.junit.jupiter.api.Timeout;

@Path("/api/company")
@ApplicationScoped
@RegisterRestClient(configKey = "usermanagement-api")
public interface UserManagementApi {

    @GET
    @Timeout(2000)
    @Retry(maxRetries = 3)
    @Path("/{companyId: [0-9]+}")
    CompanyRepresentation getCompany(@PathParam("companyId") Integer id);
}
