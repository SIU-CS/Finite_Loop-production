package edu.siu.cs.www.parkingspotfinder;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;

/**
 * Created by mohammedabdulkadir on 5/1/17.
 */
@RunWith(RobolectricTestRunner.class)
public class MapActivityTest extends TestCase {
    MapActivity activitymap;
    ActivityController<MapActivity> activityController;

    @Before
    public void setUp(){
      // activityController = Roboelectric.buildActivity(MapActivity.class);
        activitymap = activityController.create().get();

    }
    @Test
    public void test(){

    }

    private class Roboelectric {
    }
}
