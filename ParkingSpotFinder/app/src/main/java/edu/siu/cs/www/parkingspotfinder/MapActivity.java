package edu.siu.cs.www.parkingspotfinder;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.Manifest;
import android.widget.Toast;
import android.support.annotation.*;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Permission;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private Button menuButton, searchButton;
    private EditText searchBar;
    private Intent swicthActivityIntent;

    private GPSTracker gpsTracker;
    private Location mLoc;
    double lat, lon;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        menuButton = (Button) findViewById(R.id.menuButton);
        searchButton = (Button) findViewById(R.id.searchButton);
        searchBar = (EditText) findViewById(R.id.searchLocationBar);

        gpsTracker = new GPSTracker(getApplicationContext());
        mLoc = gpsTracker.getLoc();

        lat = mLoc.getLatitude();
        lon = mLoc.getLongitude();

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String loc = searchBar.getText().toString();
//                List<Address> addressList = null;
//                if(loc!=null || !loc.equals("")){
//                    Geocoder geo = new Geocoder(MapActivity.this);
//                    try {
//                        List<Address> addresses = geo.getFromLocationName(loc, 1);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
//                    Address addr = addressList.get(0);
//                    LatLng latlon = new LatLng(addr.getLatitude(), addr.getLongitude());
//                    mMap.addMarker(new MarkerOptions().position(latlon).title("Test"));
//                    mMap.animateCamera(CameraUpdateFactory.newLatLng(latlon));
//                }
            }
        });

        // Switch to the menu when the menu button is clicked
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchActivity(MapActivity.this, MenuActivity.class);
            }
        });
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(lat, lon);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Test"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    // Method assists with switching activities
    public void switchActivity(Activity current, Class switchTo) {
        swicthActivityIntent = new Intent(current, switchTo);
        startActivity(swicthActivityIntent);
    }
}

