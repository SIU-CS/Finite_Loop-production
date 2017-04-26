package edu.siu.cs.www.parkingspotfinder;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

/**
 * Created by Jacob Reed on 4/25/2017.
 */

public class UpdateInfoTest extends TestCase {
    UpdateInfo info;

    @Before
    public void setUp(){
        info = new UpdateInfo();
    }

    @Test
    public void testGetName(){
        String name;

        name = "Jake";
        assertEquals("Jake", info.getName(name));

        name = "Joe Bigman";
        assertEquals("Joe Bigman", info.getName(name));

        name = "Mikey Joe";
        assertEquals("Mikey Joe", info.getName(name));

        name = "";
        assertEquals("", info.getName(name));
    }

    @Test
    public void testGetEmail(){
        String email;

        email = "Jake3222@gmail.com";
        assertEquals("Jake3222@gmail.com", info.getName(email));

        email = "Joeyman111@yahoo.com";
        assertEquals("Joeyman111@yahoo.com", info.getName(email));

        email = "Mikeandike@siu.edu";
        assertEquals("Mikeandike@siu.edu", info.getName(email));

        email = "";
        assertEquals("", info.getName(email));
    }
}
