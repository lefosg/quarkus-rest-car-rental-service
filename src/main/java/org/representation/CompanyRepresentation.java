package org.representation;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class CompanyRepresentation {
    public Integer id;
    public String name;
    public String password;
    public String IBAN;
    public String AFM;
    public String email;
    public String phone;
    public String city;
    public String street;
    public String zipcode;
    public ChargingPolicyRepresentation policy;
}