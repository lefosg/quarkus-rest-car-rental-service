package org.infastructure.rest.representation;


import io.quarkus.runtime.annotations.RegisterForReflection;
import org.util.DamageType;

@RegisterForReflection
public class TechnicalCheckRepresentation {
    public Integer id;
    public DamageType damageType;
    public Integer rent;
}
