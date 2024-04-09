package org.domain.TechnicalCheck;

import jakarta.persistence.*;
import org.domain.Rents.Rent;
import org.util.DamageType;
import org.util.Money;
import org.util.VehicleState;

import java.util.Random;

@Entity
@DiscriminatorValue("Impl")
public class TechnicalCheckImpl extends TechnicalCheck {

    @Override
    public DamageType checkForDamage() {
        return null;
    }

    public TechnicalCheckImpl() { }

    public TechnicalCheckImpl(Rent rent) {
        this.rent = rent;
    }

    @Column(name = "damageType")
    @Enumerated(EnumType.STRING)
    private DamageType damageType;

    //domain logic

//  todo ti kanoyme edo??

//    public DamageType checkForDamage() {
//        rent.getRentedVehicle().setVehicleState(VehicleState.Service);
//        Random random= new Random();
//        int number = random.nextInt(10) + 1;
//        if( number==1 ){
//            this.damageType = calcDamage();
//            return damageType;
//        } else if(number>8 && rent.getRentedVehicle().getCountOfRents()>=1) {
//            this.damageType = calcDamage();
//            return damageType;
//        } else if(number>6 && rent.getRentedVehicle().getCountOfRents()>=2){
//            this.damageType = calcDamage();
//            return damageType;
//        }
//        rent.getRentedVehicle().setCountOfRents(rent.getRentedVehicle().getCountOfRents()+1);
//        this.damageType = DamageType.NoDamage;
//        return DamageType.NoDamage;
//    }
//
//    private DamageType calcDamage(){
//        rent.getRentedVehicle().setCountDamages(rent.getRentedVehicle().getCountDamages()+1);
//        Random damage= new Random();
//        int numberOfType= damage.nextInt(5)+1;
//        DamageType damageType = DamageType.values()[numberOfType];
//        rent.setDamageCost(new Money(rent.getRentedVehicle().getCompany().getPolicy().calculateDamageCost(damageType)));
//        return damageType;
//    }

    //getters & setters

    public DamageType getDamageType() {
        return damageType;
    }

    public void setDamageType(DamageType type) {
        this.damageType = type;
    }

    @Override
    public String toString() {
        return "damageType: " + damageType + "\n" +
                super.toString();
    }

}
