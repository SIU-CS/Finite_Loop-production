package edu.siu.cs.www.parkingspotfinder;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

/**
 * Created by Jacob Reed on 4/25/2017.
 */

public class ManageSpotTest extends TestCase {
    ManageSpot spotManager;

    @Before
    public void setUp(){
        spotManager = new ManageSpot();
    }

    @Test
    public void testCalcRate() throws Exception{
        try{
            Float testMin = 1.00f;
            Float testHour = 0.25f;

            assertEquals("1.25", spotManager.calculateRate(testMin, testHour));

            testMin = 2.00f;
            testHour = 0.50f;
            assertEquals("2.50", spotManager.calculateRate(testMin, testHour));

            testMin = 3.00f;
            testHour = 0.75f;
            assertEquals("3.75", spotManager.calculateRate(testMin, testHour));

            testMin = 0.00f;
            testHour = 0.50f;
            assertEquals("0.50", spotManager.calculateRate(testMin, testHour));

            testMin = 0.00f;
            testHour = 0.25f;
            assertEquals("0.25", spotManager.calculateRate(testMin, testHour));

            testMin = 0.00f;
            testHour = 0.00f;
            assertEquals("0.00", spotManager.calculateRate(testMin, testHour));
        }catch (Exception e){
            fail("ERROR: EXCEPTION THROWN::TEST CALCRATE");
        }
    }
}
