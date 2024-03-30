package org.infastructure.persistence;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.ws.rs.NotFoundException;
import org.domain.customer.Customer;
import org.domain.customer.CustomerRepository;

import java.util.List;
import java.util.Optional;

@RequestScoped
public class CustomerRepositoryImpl implements PanacheRepositoryBase<Customer,Integer>, CustomerRepository {

    @Override
    public EntityManager getEntityManager() {
        return PanacheRepositoryBase.super.getEntityManager();
    }

    @Override
    public void persist(Customer customer) {
        PanacheRepositoryBase.super.persist(customer);
    }

    @Override
    public Customer findById(Integer integer) {
        return PanacheRepositoryBase.super.findById(integer);
    }

    @Override
    public Optional<Customer> findByIdOptional(Integer integer) {
        return PanacheRepositoryBase.super.findByIdOptional(integer);
    }

    @Override
    public void deleteAllCustomers() {
        List<Customer> customers = listAll();
        for (Customer c : customers) {
            //c.getRents().clear();
            delete(c);
        }
    }

    @Override
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

    @Override
    public List<Customer> findBySurname(String surname) {
        if (surname == null)
            return listAll();

        return find("select customer from Customer customer where customer.surname = :surname",
        Parameters.with("surname", surname).map()).list();
    }

    @Override
    public List<Customer> findByEmail(String email) {
        if (email == null)
            return listAll();

        return find("select customer from Customer customer where customer.email = :email",
                Parameters.with("email", email).map()).list();
    }

    @Override
    public List<Customer> findByPhone(String phone) {
        if (phone == null)
            return listAll();

        return find("select phone from Customer customer where customer.phone = :phone",
                Parameters.with("phone", phone).map()).list();
    }

    @Override
    public List<Customer> findByAFM(String AFM) {
        if (AFM == null)
            return listAll();

        return find("select AFM from Customer customer where customer.AFM = :AFM",
                Parameters.with("AFM", AFM).map()).list();
    }

    @Override
    public List<Customer> findByName(String name) {
        if (name == null || name.equals(""))
            return listAll();

        return find("select name from Customer customer where customer.name = :name",
                Parameters.with("name", name).map()).list();
    }

    @Override
    public List<Customer> listAllCustomers() {
        return listAll();
    }


}
