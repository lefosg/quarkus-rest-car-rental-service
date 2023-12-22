package org.persistence;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;
import io.quarkus.security.identity.request.UsernamePasswordAuthenticationRequest;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.NotFoundException;
import org.domain.Company;
import org.domain.Customer;

import java.util.List;

@RequestScoped
public class CustomerRepository implements PanacheRepositoryBase<Customer,Integer> {

    public void deleteAllCustomers() {
        List<Customer> customers = listAll();
        for (Customer c : customers) {
            //c.getRents().clear();
            delete(c);
        }
    }

    public void deleteCustomer(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("CustomerRepository: deleteCustomer\n\tid was null");
        }
        Customer customer = findById(id);
        if (customer == null) {
            throw new NotFoundException("CustomerRepository: deleteCustomer\n\tcompany found is null");
        }
        //customer.getRents().clear();
        delete(customer);
    }

    public List<Customer> findBySurname(String surname) {
        if (surname == null)
            return listAll();

        return find("select customer from Customer customer where customer.surname = :surname",
        Parameters.with("surname", surname).map()).list();
    }
    public List<Customer> findByEmail(String email) {
        if (email == null)
            return listAll();

        return find("select customer from Customer customer where customer.email = :email",
                Parameters.with("email", email).map()).list();
    }
    public List<Customer> findByPhone(String phone) {
        if (phone == null)
            return listAll();

        return find("select phone from Customer customer where customer.phone = :phone",
                Parameters.with("phone", phone).map()).list();
    }
    public List<Customer> findByAFM(String AFM) {
        if (AFM == null)
            return listAll();

        return find("select AFM from Customer customer where customer.AFM = :AFM",
                Parameters.with("AFM", AFM).map()).list();
    }
    public List<Customer> findByName(String name) {
        if (name == null || name.equals(""))
            return listAll();

        return find("select name from Customer customer where customer.name = :name",
                Parameters.with("name", name).map()).list();
    }



}
