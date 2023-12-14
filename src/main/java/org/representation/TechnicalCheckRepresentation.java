package org.representation;


import io.quarkus.runtime.annotations.RegisterForReflection;
import org.domain.Rent;

@RegisterForReflection
public class TechnicalCheckRepresentation {
    public  Integer id;
    public Rent rent;
}
