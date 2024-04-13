package org.domain.TechnicalCheck;

import jakarta.persistence.*;
import org.domain.Rents.Rent;
import org.util.DamageType;

@Entity
@Table(name = "TECHNICAL_CHECK")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(
        name="type",
        discriminatorType=DiscriminatorType.STRING
)
public abstract class TechnicalCheck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    protected Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="rent_id")
    protected Rent rent;

    public abstract DamageType checkForDamage();

    public TechnicalCheck() { }

    public TechnicalCheck(Rent rent) {
        this.rent = rent;
    }

    //public abstract DamageType checkForDamage();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Rent getRent() {
        return rent;
    }

    public void setRent(Rent rent) {
        this.rent = rent;
    }

    @Override
    public String toString() {
        return "id: " + id + "\n" +
                "rent: " + rent.getId();
    }

}
