package org.old.jpa;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public class JPATest {
    EntityManager em;

    @BeforeEach
    public void setup() {

        Initializer initializer = new Initializer();
        initializer.prepareData();

        em = JPAUtil.getCurrentEntityManager();

    }

    @AfterEach
    public void tearDown() {
        em.close();
    }
}
