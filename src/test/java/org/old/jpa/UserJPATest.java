package org.old.jpa;

import org.domain.User;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class UserJPATest extends JPATest {

    @Test
    public void listUsers() {
        List<User> users =  em.createQuery("select u from User u").getResultList();
        assertEquals(4, users.size());
    }
}
