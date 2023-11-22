package org.domain;

import jakarta.persistence.*;
import org.util.DamageType;
import org.util.Money;
import org.util.VehicleType;

import java.util.Random;

@Entity
@Table(name = "TECHICAL_CHECK")
public class TechnicalCheck {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    protected Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    private Rent rent;

    public TechnicalCheck(){

    }
    public TechnicalCheck(Rent rent) {
        this.rent = rent;
    }

    //domain logic

    public DamageType checkForDamage() {
        Random random= new Random();
        int number = random.nextInt(10) + 1;
        if(number==1 ){
            return calcDamage();
        }else if(number>8 && rent.getRentedVehicle().getCountDamages()>1){
            return calcDamage();
        }else if(number>6 && rent.getRentedVehicle().getCountDamages()>2){
            return calcDamage();
        }
        return DamageType.NoDamage;
    }
    public DamageType calcDamage(){
        rent.getRentedVehicle().setCountDamages(rent.getRentedVehicle().getCountDamages()+1);
        Random damage= new Random();
        int numberOfType= damage.nextInt(5)+1;
        DamageType damageType = DamageType.values()[numberOfType];
        rent.setDamageCost(new Money(rent.getRentedVehicle().getCompany().getPolicy().calculateDamageCost(rent.getRentedVehicle().getVehicleType(),damageType)));
        return damageType;
    }

    //getters & setters


    public Rent getRent() {
        return rent;
    }

    public void setRent(Rent rent) {
        this.rent = rent;
    }
}
