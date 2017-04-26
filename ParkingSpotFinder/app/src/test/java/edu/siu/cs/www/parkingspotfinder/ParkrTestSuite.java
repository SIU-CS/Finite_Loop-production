package edu.siu.cs.www.parkingspotfinder;

import junit.framework.TestSuite;

import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

/**
 * Created by Jacob Reed on 4/25/2017.
 */

public class ParkrTestSuite extends TestSuite {
    public static TestSuite suite(){
        Class[] listOfTest = {InitialPaymentTest.class, ManageSpotTest.class, UpdateInfoTest.class};

        TestSuite suite = new TestSuite(listOfTest);

        return suite;
    }
}