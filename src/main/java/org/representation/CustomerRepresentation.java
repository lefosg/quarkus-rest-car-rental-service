package org.representation;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.time.LocalDate;

@RegisterForReflection
public class CustomerRepresentation {
public String  surname;
public String number;
public LocalDate expirationDate;
public String holderName;


}
