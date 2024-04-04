package org.infastructure.service.user_management;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.infastructure.service.user_management.representation.CompanyRepresentation;

@Path("/api/company")
@ApplicationScoped
@RegisterRestClient(configKey = "usermanagement-api")
public interface UserManagementApi {

    @GET
    @Path("/{companyId: [0-9]+}")
    CompanyRepresentation getCompany(@PathParam("companyId") Integer id);
}
