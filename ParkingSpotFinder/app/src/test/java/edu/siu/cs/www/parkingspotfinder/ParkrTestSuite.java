package edu.siu.cs.www.parkingspotfinder;

import junit.framework.TestSuite;

/**
 * Created by Jacob Reed on 4/25/2017.
 */

public class ParkrTestSuite extends TestSuite {
    public static TestSuite suite(){
        Class[] listOfTest = {InitialPaymentTest.class, ManageSpotTest.class};

        TestSuite suite = new TestSuite(listOfTest);

        return suite;
    }
}
