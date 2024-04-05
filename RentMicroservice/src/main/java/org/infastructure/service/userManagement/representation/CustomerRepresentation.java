package org.infastructure.service.userManagement.representation;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient
public class CustomerRepresentation {
    public Integer id;
    public String name;
    public String AFM;
    public String email;
    public String phone;
    public String surname;
    public String password;
    public String number;
    public String expirationDate;
    public String holderName;
    public String city;
    public String street;
    public String zipcode;
}
