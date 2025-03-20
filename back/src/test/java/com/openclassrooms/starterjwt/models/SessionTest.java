package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SessionTest {

    @Test
    void testSessionEntity(){
        Session session = new Session();
        session.equals(new Session());
        session.hashCode();
        session.toString();
        assertNotNull(session.toString());
    }

    @Test
    void testSessionEntityBuilder(){
        Session session = new Session();
        session.equals(Session.builder().build());
        assertNotNull(session.toString());
    }
}