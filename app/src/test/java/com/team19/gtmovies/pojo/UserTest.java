package com.team19.gtmovies.pojo;
import com.team19.gtmovies.exception.IllegalUserException;

import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

/**
 * Created by Shayan Pirani on 4/3/2016.
 */
public class UserTest {
    @Test
    public void testUserConstructorBestCase() throws Exception {
        User u = new User("johnson", "password", "email@good.com", "Johnny");
        assertEquals("johnson", u.getUsername());
        assertEquals("password", u.getPassword());
        assertEquals("Johnny", u.getName());
        assertEquals("none", u.getBio());
        assertEquals("none", u.getMajor());
        assertEquals(1, u.getPermission());
        assertEquals(false, u.getHasProfile());
    }

    /* NOTE: email attribute added to user class after this class was made.   */
    @Test (expected = IllegalArgumentException.class)
    public void testNullValues1() throws Exception {
        User u = new User(null, "password", "email@good.com", "Johnny");
    }

    @Test (expected = IllegalArgumentException.class)
    public void testNullValues2() throws Exception {
        User u = new User("johnson", null, "email@good.com", "Johnny");
    }

    @Test (expected = IllegalArgumentException.class)
    public void testNullValues3() throws Exception {
        User u = new User("johnson", "password", "email@good.com", null);
    }

    @Test (expected = IllegalUserException.class)
    public void testShortUsername() throws Exception {
        User u = new User("j", "password", "email@good.com", "Johnny");
    }

    @Test (expected = IllegalUserException.class)
    public void testShortPassword() throws Exception {
        User u = new User("johnson", "p", "email@good.com", "Johnny");
    }
}
