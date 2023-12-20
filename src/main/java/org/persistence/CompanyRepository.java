package org.persistence;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.Id;
import jakarta.ws.rs.NotFoundException;
import org.domain.Company;
import org.domain.Vehicle;

import java.util.List;

@RequestScoped
public class CompanyRepository implements PanacheRepositoryBase<Company, Integer> {

    public void deleteAllCompanies() {
        List<Company> companies = listAll();
        for (Company c : companies) {
            c.getVehicles().clear();
            delete(c);
        }
    }

    public void deleteCompany(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("CompanyRepository: deleteCompany\n\tid was null");
        }
        Company company = findById(id);
        if (company == null) {
            throw new NotFoundException("CompanyRepository: deleteCompany\n\tcompany found is null");
        }
        company.getVehicles().clear();
        delete(company);
    }

    public List<Company> findByCity(String city) {
        if (city == null || city.equals(""))
            return listAll();

        return find("select company from Company company where company.city = :city",
                Parameters.with("city", city).map()).list();
    }

    public List<Company> findByName(String name) {
        if (name == null || name.equals(""))
            return listAll();

        return find("select name from Company company where company.name = :name",
                Parameters.with("name", name).map()).list();
    }

    public List<Company> findByPhone(String phone) {
        if (phone == null || phone.equals(""))
            return listAll();

        return find("select phone from Company company where company.phone = :phone",
                Parameters.with("phone", phone).map()).list();
    }

    public List<Company> findByEmail(String email) {
        if (email == null || email.equals(""))
            return listAll();

        return find("select email from Company company where company.email = :email",
                Parameters.with("email", email).map()).list();
    }

    public List<Company> findByAFM(String AFM) {
        if (AFM == null || AFM.equals(""))
            return listAll();

        return find("select AFM from Company company where company.AFM = :AFM",
                Parameters.with("AFM", AFM).map()).list();
    }
}

