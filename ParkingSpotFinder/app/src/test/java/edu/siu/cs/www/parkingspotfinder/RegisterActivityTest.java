package edu.siu.cs.www.parkingspotfinder;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import static junit.framework.Assert.assertEquals;
/**
 * Created by alejandro on 5/3/17.
 */

public class RegisterActivityTest extends RegisterActivitty {
    RegisterActivitty checkEmail;

    @Before
    public void setUp(){
        checkEmail = new RegisterActivitty();
    }

    @Test
    public void testGetEmail() {
        String email;

        email = "agnarvaez@icloud.com";
        assertEquals("agnarvaez@icloud.com", checkEmail.getEmail(email));

        email = "";
        assertEquals("", checkEmail.getEmail(email));
    }

    @Test
    public void testGetName() {
        String name;

        name = "Alejandro";
        assertEquals("Alejandro", checkEmail.getName(name));

        name = "Alejandro Narvaez";
        assertEquals("Alejandro Narvaez", checkEmail.getName(name));

        name = "";
        assertEquals("", checkEmail.getName(name));
    }

}
