package org.old.jpa;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;


public class JPAUtilTest {

    @Test
    public void testGetCurrentEntityManager(){
        EntityManager em = JPAUtil.getCurrentEntityManager();
        assertNotNull(em);
    }

}
