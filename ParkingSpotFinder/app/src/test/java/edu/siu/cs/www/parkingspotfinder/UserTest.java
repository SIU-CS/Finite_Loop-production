package edu.siu.cs.www.parkingspotfinder;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

/**
 * Created by mohammedabdulkadir on 4/29/17.
 */

public class UserTest extends TestCase {
    private User userinfo;

    @Before
    public void setUp() {
        userinfo = new User();
    }

    @Test
    public void testGetName() {
        String name;

        name = "blue";
        assertEquals("blue", name);

        name = "mohd";
        assertEquals("mohd", name);

        name = "";
        assertEquals("", name);
        

    }

    @Test
    public void testGetEmail() {
        String email;

        email = "blue@siu.edu";
        assertEquals("blue@siu.edu", email);

        email = "mohd@siu.edu";
        assertEquals("mohd@siu.edu", email);

        email = "";
        assertEquals("", email);



    }
}

