package edu.siu.cs.www.parkingspotfinder;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AccountActivity extends AppCompatActivity {

    private FirebaseDatabase mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference ref;

    private TextView nameText;
    private TextView emailText;
    private Button backArrowButton;
    private Button updateAccntButton;

    private String name;
    private String email;
    private String userID;

    private final static String TAG = "ACCOUNT_ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        // Get the instance of the user and the database reference
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        ref = mDatabase.getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();

        // Create the UI items and bind to the code
        nameText = (TextView) findViewById(R.id.name);
        emailText = (TextView) findViewById(R.id.email);
        backArrowButton = (Button) findViewById(R.id.backArrowButton);
        updateAccntButton = (Button) findViewById(R.id.updateAccountButton);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    Log.d(TAG, "Logged In");
                } else {
                    Log.d(TAG, "Failed to login");
                }
            }
        };

        // Add listener to get realtime data from the database
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                displayData(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "AccountActivity::cannotGetRefSnapshot");
            }
        });

        backArrowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent menuActivityStart = new Intent(AccountActivity.this, MenuActivity.class);
                startActivity(menuActivityStart);
            }
        });

        updateAccntButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent updateAccountAccountActivity = new Intent(AccountActivity.this, UpdateInfo.class);
                startActivity(updateAccountAccountActivity);
            }
        });

    }

    // Get the data from the database for the specific user, create new user object, display info.
    private void displayData(DataSnapshot dataSnapshot) {
        for(DataSnapshot s : dataSnapshot.getChildren()){
            User user = new User();

            // Get the information from the snapshot and add it to the new User object.
            user.setName(s.child(userID).getValue(User.class).getName());
            user.setEmail(s.child(userID).getValue(User.class).getName());

            // Set UI text
            nameText.setText(user.getName());
            emailText.setText(user.getEmail());
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
