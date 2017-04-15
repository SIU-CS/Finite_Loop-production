package edu.siu.cs.www.parkingspotfinder;

import android.graphics.Color;
import android.support.annotation.IntegerRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SelectSpotActivity extends AppCompatActivity {

    private final String TAG = "SELECT_SPOT_ACTRIVITY::";

    private ListView spots;

    private DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_spot);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        ArrayList<String> spotNames = new ArrayList<String>();
        final ArrayAdapter<String> listViewAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spotNames);
        Bundle extras = getIntent().getExtras();

        spots = (ListView) findViewById(R.id.spotList);
        spots.setAdapter(listViewAdapter);

        if (extras != null){
            String tag = extras.getString("lot-tag");
            mRef = database.getReference().child("lots").child(tag).child("spots");
            Log.d(TAG, "GETTING::"+tag);
            mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot spot : dataSnapshot.getChildren()){
                        int index = Integer.valueOf(spot.getKey().toString()) - 1;
                        listViewAdapter.add(spot.child("name").getValue().toString());
                        Log.d(TAG, "INDEX::"+spots.getCount());
                        if (spot.child("state").getValue().toString().equals("READY")){
                            spots.getAdapter().getView(index, null, spots).setEnabled(true);
                            Log.d(TAG, String.valueOf(spots.getChildCount()));
                        } else {
                            spots.getAdapter().getView(index, null, spots).setClickable(false);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    return;
                }
            });
        }
    }
}
