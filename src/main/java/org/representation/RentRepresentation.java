package org.representation;

import io.quarkus.runtime.annotations.RegisterForReflection;
import org.util.Money;
import org.util.RentState;
import java.time.LocalDate;

@RegisterForReflection
public class RentRepresentation {
    public Integer id;
    public String startDate;
    public String endDate;
    public RentState rentState;
    public Money fixedCost;
    public float miles;
    public Money mileageCost;
    public Money damageCost;
    public Money totalCost;
    public Integer rentedVehicle;
    public Integer technicalCheck;
    public Integer customer;
    //todo in domain logic, embeddables -> new
}
