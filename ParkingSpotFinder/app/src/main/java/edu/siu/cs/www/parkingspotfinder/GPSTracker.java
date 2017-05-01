package edu.siu.cs.www.parkingspotfinder;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;


/**
 * Created by jacobreed on 2/23/17.
 */

public class GPSTracker extends Service implements LocationListener{

    private final Context context;

    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;

    public Location getLoc(){
        try{
            locManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
            isGPSEnabled = locManager.isProviderEnabled(locManager.GPS_PROVIDER);
            isNetworkEnabled = locManager.isProviderEnabled(locManager.NETWORK_PROVIDER);

            if(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                if(isGPSEnabled){
                    if (loc == null){
                        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000,10,this);
                        if (locManager != null){
                            loc = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        }
                    }
                }

                if (loc == null){
                    if (isNetworkEnabled){
                        locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000,10,this);
                        if (locManager != null) {
                            loc = locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        }
                    }
                }
            }
        } catch (Exception ex) {

        }
        return loc;
    }

    Location loc;
    protected LocationManager locManager;

    public GPSTracker(Context context){
        this.context = context;
    }

    @Override
    public void onLocationChanged(Location local){

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extra){

    }

    @Override
    public void onProviderEnabled(String provider){

    }

    @Override
    public void onProviderDisabled(String provider){

    }

    @Override
    public IBinder onBind(Intent intent){
        return null;
    }
}
