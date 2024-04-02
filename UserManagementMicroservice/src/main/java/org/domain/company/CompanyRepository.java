package org.domain.company;

import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

public interface CompanyRepository {

    void deleteAllCompanies();

    void deleteCompany(Integer id);

    List<Company> findByCity(String city);

    List<Company> findByName(String name);

    List<Company> findByPhone(String phone);

    List<Company> findByEmail(String email);

    List<Company> findByAFM(String AFM);

    Optional<Company> findByCompanyIdOptional(Integer integer);

    List<Company> listAllCompanies();

    void persistCompany(Company company);

    EntityManager getCompanyEntityManager();

    Company findCompanyById(Integer integer);
}
