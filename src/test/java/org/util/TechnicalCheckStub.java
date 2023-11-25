package org.util;

import org.domain.Rent;
import org.domain.TechnicalCheck;

public class TechnicalCheckStub implements TechnicalCheck {

    private Rent rent;
    private static int count=-1;

    public TechnicalCheckStub() { }

    public TechnicalCheckStub(Rent rent) {
        this.rent = rent;
    }

    @Override
    public DamageType checkForDamage() {
        System.out.println(count);
        count++;
        if (count == 0) {
            return DamageType.NoDamage;
        } else if (count == 1) {
            return DamageType.Tyres;
        } else if (count == 2) {
            return DamageType.Machine;
        } else if (count == 3) {
            return DamageType.Glasses;
        } else if (count == 4) {
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
