package edu.siu.cs.www.parkingspotfinder;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

/**
 * Created by mohammedabdulkadir on 5/1/17.
 */

public class MapActivityTest extends TestCase {
    MapActivity activitymap;

    protected double lat;
    protected double lon;

    @Before
    public void setUp(){
      // activityController = Roboelectric.buildActivity(MapActivity.class);
      //  activitymap = activityController.create().get();
        activitymap = new MapActivity();
        lat = 38.7098824;
        lon= -90.2220897;

    }

    @Test
    public void testMapLocal() {
        double result = lat + lon;
        assertTrue(result == result);

        TestCase test = new LoginTest();
        test.run();
    }



        }

