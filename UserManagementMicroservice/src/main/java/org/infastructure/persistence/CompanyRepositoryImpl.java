package org.infastructure.persistence;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.ws.rs.NotFoundException;
import org.domain.company.Company;
import org.domain.company.CompanyRepository;


import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class CompanyRepositoryImpl implements PanacheRepositoryBase<Company, Integer>, CompanyRepository {

    @Override
    public Optional<Company> findByCompanyIdOptional(Integer integer) {
        return findByIdOptional(integer);
    }

    @Override
    public List<Company> listAllCompanies() {
       return listAll();
    }

    @Override
    public Company findCompanyById(Integer integer) {
        return findById(integer);
    }

    @Override
    public void persistCompany(Company company) {
        persist(company);
    }

    @Override
    public EntityManager getCompanyEntityManager() {
        return getEntityManager();
    }



    @Override
    public void deleteAllCompanies() {
        List<Company> companies = this.listAll();
        for (Company c : companies) {
            //todo c.getVehicles().clear();
            delete(c);
        }
    }

    @Override
    public void deleteCompany(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("CompanyRepository: deleteCompany\n\tid was null");
        }
        Company company = findById(id);
        if (company == null) {
            throw new NotFoundException("CompanyRepository: deleteCompany\n\tcompany found is null");
        }
        //todo company.getVehicles().clear();
        delete(company);
    }

    @Override
    public List<Company> findByCity(String city) {
        if (city == null || city.equals(""))
            return listAll();

        return find("select company from Company company where company.city = :city",
                Parameters.with("city", city).map()).list();
    }

    @Override
    public List<Company> findByName(String name) {
        if (name == null || name.equals(""))
            return listAll();

        return find("select name from Company company where company.name = :name",
                Parameters.with("name", name).map()).list();
    }

    @Override
    public List<Company> findByPhone(String phone) {
        if (phone == null || phone.equals(""))
            return listAll();

        return find("select phone from Company company where company.phone = :phone",
                Parameters.with("phone", phone).map()).list();
    }

    @Override
    public List<Company> findByEmail(String email) {
        if (email == null || email.equals(""))
            return listAll();

        return find("select email from Company company where company.email = :email",
                Parameters.with("email", email).map()).list();
    }

    @Override
    public List<Company> findByAFM(String AFM) {
        if (AFM == null || AFM.equals(""))
            return listAll();

        return find("select AFM from Company company where company.AFM = :AFM",
                Parameters.with("AFM", AFM).map()).list();
    }
}

