package org.persistence;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.security.identity.request.UsernamePasswordAuthenticationRequest;
import jakarta.enterprise.context.RequestScoped;
import org.domain.Company;
import org.domain.Customer;
import org.domain.Vehicle;

import java.util.List;

@RequestScoped
public class VehicleRepository implements PanacheRepositoryBase<Vehicle,Integer>{
}
