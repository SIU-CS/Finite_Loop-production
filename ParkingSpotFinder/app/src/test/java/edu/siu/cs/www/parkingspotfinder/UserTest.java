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
    public void setUp(){
        userinfo = new User();
    }

    @Test
    public void testGetName(){
        String name;

        name = "Moh'd";
        assertEquals("Moh'd", userinfo.getName(name));

        name = "Jake";
        assertEquals("Jake", userinfo.getName(name));

        name = "Maha";
        assertEquals("Maha", userinfo.getName(name));

        name = "Alejandro";
        assertEquals("Alejandro", userinfo.getName(name));

        name = "";
        assertEquals("", userinfo.getName(name));

    }
    @Test
   public void testGetEmail() {
        String email;

        email = "mohd.jafar@siu.com";
        assertEquals("mohd.jafar@siu.com", userinfo.getName(email));

        email = "Jake@siu.com";
        assertEquals("Jake@siu.com", userinfo.getName(email));

        email = "maha@siu.com";
        assertEquals("maha@siu.com", userinfo.getName(email));

        email = "alejandro@siu.com";
        assertEquals("alejandro@siu.com", userinfo.getName(email));

        email = "";
        assertEquals("", userinfo.getName(email));

    }
}

