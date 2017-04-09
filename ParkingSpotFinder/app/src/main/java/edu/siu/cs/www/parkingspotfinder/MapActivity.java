package edu.siu.cs.www.parkingspotfinder;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
public class MapActivity extends FragmentActivity implements OnMapReadyCallback {
    DatabaseReference myRef;
    private GoogleMap mMap;

    private final String TAG = "MAP_ACTIVITY";

    private Button menuButton, searchButton, zoomInButton, zoomOutButton;
    private EditText searchBar;
    private Intent swicthActivityIntent;
    private MarkerOptions mapMarker;

    private GPSTracker gpsTracker;
    private Location mLoc;
    private double lat, lon;
    private LatLng latLng;
    private String addressText;
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
        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("lots");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            //marker to show parking lots from firebase
            public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot post : dataSnapshot.getChildren()) {
                        Double latitude = (Double) post.child("local").child("lat").getValue();
                        Double longitude = (Double) post.child("local").child("long").getValue();
                        String name = (String) post.child("properties").child("name").getValue();
                        String tag = (String) post.getKey();

                        LatLng local = new LatLng(latitude, longitude);

                        mMap.addMarker(new MarkerOptions().position(local).title(name)
                                .icon(BitmapDescriptorFactory
                                        .defaultMarker(BitmapDescriptorFactory.HUE_BLUE)))
                                .setTag(tag);

                        Log.d(TAG, "MARKER_MADE::"+tag);
                    }
                }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        menuButton = (Button) findViewById(R.id.menuButton);
        searchButton = (Button) findViewById(R.id.searchButton);
        zoomInButton = (Button) findViewById(R.id.zoomInButton);
        zoomOutButton = (Button) findViewById(R.id.zoomOutButton);
        searchBar = (EditText) findViewById(R.id.searchLocationBar);

        zoomInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float zoomLevel = mMap.getCameraPosition().zoom;
                mMap.animateCamera(CameraUpdateFactory.zoomTo(zoomLevel + 1));
            }
        });

        zoomOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float zoomLevel = mMap.getCameraPosition().zoom;
                mMap.animateCamera(CameraUpdateFactory.zoomTo(zoomLevel - 1));
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String g = searchBar.getText().toString();

                Geocoder geocoder = new Geocoder(getBaseContext());
                List<Address> addresses = null;

                try {
                    // Getting a maximum of 3 Address that matches the input
                    // text
                    addresses = geocoder.getFromLocationName(g, 3);
                    if (addresses != null && !addresses.equals(""))
                        search(addresses);

                } catch (Exception e) {

                }

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

//        gpsTracker = new GPSTracker(getApplicationContext());
//        mLoc = gpsTracker.getLoc();
//
//        lat = mLoc.getLatitude();
//        lon = mLoc.getLongitude();

        // Add a marker in Sydney and move the camera
        LatLng loc = new LatLng(38.7098824, -90.2220897);
        mMap.addMarker(new MarkerOptions().position(loc).title("Test"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(5));

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference spotRef = database.getReference().child("lots");

                spotRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot s : dataSnapshot.getChildren()){
                            return;
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        return;
                    }
                });

                if (marker.getTag() != null)
                    Toast.makeText(MapActivity.this, marker.getTag().toString(), Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    // Method assists with switching activities
    public void switchActivity(Activity current, Class switchTo) {
        swicthActivityIntent = new Intent(current, switchTo);
        startActivity(swicthActivityIntent);
    }

    protected void search(List<Address> addresses) {

        Address address = (Address) addresses.get(0);
        lon = address.getLongitude();
        lat = address.getLatitude();
        latLng = new LatLng(address.getLatitude(), address.getLongitude());

        addressText = String.format(
                "%s, %s",
                address.getMaxAddressLineIndex() > 0 ? address
                        .getAddressLine(0) : "", address.getCountryName());

        mapMarker = new MarkerOptions();

        mapMarker.position(latLng);
        mapMarker.title(addressText);

        mMap.clear();
        mMap.addMarker(mapMarker);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(100));

        //permission to set and use current 'location
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }
        mMap.setMyLocationEnabled(true);

    }


}

