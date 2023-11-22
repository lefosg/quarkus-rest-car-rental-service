package org.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "TECHICAL_CHECK")
public class TechnicalCheck {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    protected Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    private Rent rent;

    public TechnicalCheck(Integer id, Rent rent) {
        this.id = id;
        this.rent = rent;
    }

    //domain logic

    public void checkForDamage() {
        //do sth
    }

    //getters & setters

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
}
