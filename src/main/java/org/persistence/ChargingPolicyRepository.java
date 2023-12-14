package org.persistence;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.security.identity.request.UsernamePasswordAuthenticationRequest;
import jakarta.enterprise.context.RequestScoped;
import org.domain.ChargingPolicy;
import org.domain.Company;
import org.domain.Customer;

import java.util.List;

@RequestScoped
public class ChargingPolicyRepository implements PanacheRepositoryBase<ChargingPolicy, Long>{
}
