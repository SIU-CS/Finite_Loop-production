package edu.siu.cs.www.parkingspotfinder;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by alejandro on 5/4/17.
 */

public class LoginActivityTest extends LoginActivity {
    LoginActivity email;

    @Before
    public void setUp(){
        email = new LoginActivity();
    }

    @Test
    public void testGetSearch(){
        String username;

        username = "alejandro";
        assertEquals("alejandro", email.getLogin(username));

        username = "maha";
        assertEquals("maha", email.getLogin(username));

        username = "jake";
        assertEquals("jake", getLogin(username));

        username = "";
        assertEquals("", email.getLogin(username));
    }
}
