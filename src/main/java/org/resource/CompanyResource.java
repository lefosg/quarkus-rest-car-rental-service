package org.resource;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("company")
public class CompanyResource {

    @GET
    public String listAllCompanies() {
        return "all companies 0_0";
    }


}
