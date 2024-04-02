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

    Optional<Company> findByIdOptional(Integer integer);

    //List<Company> listAll();

    //void persist(Company company);

    //EntityManager getEntityManager();

    //Company findById(Integer integer);
}
