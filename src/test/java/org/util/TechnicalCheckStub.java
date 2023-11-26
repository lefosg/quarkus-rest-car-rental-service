package org.util;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import org.domain.Rent;
import org.domain.TechnicalCheck;

/**
 * A stub for the TechnicalCheck abstract class. Used for testing the damage costs
 * for rents.
 */
@Entity
@DiscriminatorValue("Stub")
public class TechnicalCheckStub extends TechnicalCheck {

    int count=-1;

    public TechnicalCheckStub() {
        super();
    }

    public TechnicalCheckStub(Rent rent) {
        super(rent);
    }

    /**
     * Every time it is called, it returns a damage type according to a counter.
     * The order of returns is the same as with the if statements below.
     * @return the damage type of the broken vehicle
     */
    @Override
    public DamageType checkForDamage() {
        //System.out.println(count);
        count++;
        if (count%6 == 0) {
            return DamageType.NoDamage;
        } else if (count%6 == 1) {
            return DamageType.Tyres;
        } else if (count%6 == 2) {
            return DamageType.Machine;
        } else if (count%6 == 3) {
            return DamageType.Glasses;
        } else if (count%6 == 4) {
            return DamageType.Scratches;
        } else {
            return DamageType.Interior;
        }
    }

    public void clear() {
        count = -1;
    }

    public Rent getRent() {
        return rent;
    }

    public void setRent(Rent rent) {
        this.rent = rent;
    }
}
