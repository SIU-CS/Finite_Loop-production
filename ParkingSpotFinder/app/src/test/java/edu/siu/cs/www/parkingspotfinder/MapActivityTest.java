package edu.siu.cs.www.parkingspotfinder;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by alejandro on 5/3/17.
 */

public class MapActivityTest extends TestCase{
    MapActivity mapsearch;

    @Before
    public void setUp(){
        mapsearch = new MapActivity();
    }

    @Test
    public void testGetSearch(){
        String search;

        search = "Carbondale";
        assertEquals("Carbondale", mapsearch.getSearch(search));

        search = "St Louis";
        assertEquals("St Louis", mapsearch.getSearch(search));

        search = "1263 Lincoln Dr";
        assertEquals("1263 Lincoln Dr", mapsearch.getSearch(search));

        search = "";
        assertEquals("", mapsearch.getSearch(search));

    }
}
