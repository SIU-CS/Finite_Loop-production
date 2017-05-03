package edu.siu.cs.www.parkingspotfinder;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

/**
 * Created by mohammedabdulkadir on 5/3/17.
 */

public class LoginTest extends TestCase {
    //LoginActivity login;
    protected double sum1;
    protected double sum2;


    @Before
    protected void setUp() {
        sum1 = 4.0;
        sum2 = 5.0;
    }

    @Test
    public void testAdd() {
        double result = sum1 + sum2;
        assertTrue(result == 9.0);

        TestCase test = new LoginTest();
        test.run();
    }
}
