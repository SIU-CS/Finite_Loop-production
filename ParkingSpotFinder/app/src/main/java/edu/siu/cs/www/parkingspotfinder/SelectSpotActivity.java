package edu.siu.cs.www.parkingspotfinder;

import android.graphics.Color;
import android.support.annotation.BoolRes;
import android.support.annotation.IntegerRes;
import android.support.annotation.StringDef;
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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class SelectSpotActivity extends AppCompatActivity {

    private final String TAG = "SELECT_SPOT_ACTRIVITY::";
    private final Boolean DEBUG = true;

    private ListView spots;

    private ArrayList<String> spotNames = new ArrayList<String>();
    private ArrayAdapter<String> listViewAdapter;

    private DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_spot);

        spots = (ListView) findViewById(R.id.spotList);

        Bundle extras = getIntent().getExtras();
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        if (extras != null){
            String tag = extras.getString("lot-tag");
            mRef = database.getReference().child("lots").child(tag).child("spots");
            Log.d(TAG, "GETTING::"+tag);
            mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot spot : dataSnapshot.getChildren()){
//                        int index = Integer.valueOf(spot.getKey().toString()) - 1;
                        spotNames.add(spot.child("name").getValue().toString());

                        if(DEBUG){
                            Log.d(TAG, Arrays.toString(spotNames.toArray()));
                        }
                    }
                    listViewAdapter = new ArrayAdapter<String>
                            (SelectSpotActivity.this, android.R.layout.simple_spinner_dropdown_item, spotNames);
                    spots.setAdapter(listViewAdapter);

                    if(DEBUG){
                        Log.d(TAG, String.valueOf(listViewAdapter.getCount()));
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
