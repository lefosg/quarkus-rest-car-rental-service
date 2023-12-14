package org.representation;

import io.quarkus.runtime.annotations.RegisterForReflection;
import org.util.Money;
import org.util.RentState;
import java.time.LocalDate;

@RegisterForReflection
public class RentRepresentation {
    public Integer id;
    public LocalDate startDate;
    public LocalDate endDate;
    public RentState rentState;
    public Money fixedCost;
    public Money mileageCost;
    public Money damageCost;
    public Money totalCost;
}
