package org.util;

import jakarta.persistence.DiscriminatorValue;
import org.domain.Rent;
import org.domain.TechnicalCheck;

@DiscriminatorValue("Stub")
public class TechnicalCheckStub extends TechnicalCheck {

    private static int count=-1;

    public TechnicalCheckStub() {
        super();
    }

    public TechnicalCheckStub(Rent rent) {
        super(rent);
    }

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

    public Rent getRent() {
        return rent;
    }

    public void setRent(Rent rent) {
        this.rent = rent;
    }
}