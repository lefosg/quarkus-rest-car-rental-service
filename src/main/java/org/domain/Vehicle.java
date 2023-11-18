package org.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.util.Money;

import java.util.*;

@Entity
@Table(name="Vehicles")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name="model",length =30)
    private String model;
    @Column(name="year", length=30)
    private int year;
    @Column(name="manufacturer",length =30)
    private String manufacturer;
    @Column(name="plate_number",length =30)
    private String plate_number;
    @Column(name="vehicle_type",length =30)
    private VehicleType vehicleType;
    @Column(name="vehicle_state",length =30)
    private VehicleState vehicleState;
    @Column(name="count_damages",length =30)
    private int count;
    @Column(name="fixed_charge",length = 30)
    private Money money;





}
