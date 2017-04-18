package edu.siu.cs.www.parkingspotfinder;

import com.google.android.gms.awareness.state.Weather;

/**
 * Created by jacobreed on 4/18/17.
 */

public class Spot {
    public int icon;
    public String spotName;

    public Spot(){
        super();
    }

    public Spot(int icon, String name){
        super();
        this.icon = icon;
        this.spotName = name;
    }
}
