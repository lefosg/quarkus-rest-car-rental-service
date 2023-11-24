package org.persistence;

import jakarta.persistence.EntityTransaction;
import jakarta.persistence.RollbackException;
import org.domain.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.util.DamageType;
import org.util.Money;
import org.util.VehicleType;

import java.time.LocalDate;


import java.util.LinkedHashMap;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class RentJPATest extends JPATest{

    @Test
    public void test1() {
        List<Rent> rents = em.createQuery("select r from Rent r").getResultList();
        assertEquals(1, rents.size());

    }
}
