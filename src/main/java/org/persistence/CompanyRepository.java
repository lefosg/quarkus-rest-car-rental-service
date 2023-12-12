package org.persistence;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import org.domain.Company;

public class CompanyRepository implements PanacheRepository<Company> {
}

