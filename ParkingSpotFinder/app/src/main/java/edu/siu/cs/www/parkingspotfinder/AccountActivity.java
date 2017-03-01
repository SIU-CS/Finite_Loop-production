package edu.siu.cs.www.parkingspotfinder;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
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
    private FirebaseUser user;
    private DatabaseReference ref;
    private DatabaseReference userRef;

    private TextView nameText;
    private TextView emailText;
    private Button backArrowButton;
    private Button updateAccntButton;
    private Button deleteAccountButton;

    private String name;
    private String email;
    private String userID;

    final Context c = this;

    private final static String TAG = "ACCOUNT_ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        // Get the instance of the user and the database reference
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        ref = mDatabase.getReference();
        user = mAuth.getCurrentUser();
        userID = user.getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userID);

        // Create the UI items and bind to the code
        nameText = (TextView) findViewById(R.id.name);
        emailText = (TextView) findViewById(R.id.email);
        backArrowButton = (Button) findViewById(R.id.backArrowButton);
        updateAccntButton = (Button) findViewById(R.id.updateAccountButton);
        deleteAccountButton = (Button) findViewById(R.id.deleteAccountButton);

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

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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

        deleteAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflaterAndroid = LayoutInflater.from(c);
                View mView = layoutInflaterAndroid.inflate(R.layout.user_input_dialog_box, null);
                AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(c);
                alertDialogBuilderUserInput.setView(mView);

                final EditText userInputDialogEditText = (EditText) mView.findViewById(R.id.userInputDialog);

                alertDialogBuilderUserInput
                        .setCancelable(false)
                        .setPositiveButton("Delete Account", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                if(userInputDialogEditText.length() != 0) {
                                    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(),
                                            userInputDialogEditText.getText().toString());

                                    user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Intent startLoginActivity = new Intent(AccountActivity.this, LoginActivity.class);
                                            startActivity(startLoginActivity);
                                            user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Log.d(TAG, "DELETE_ACCOUNT_AUTH");
                                                }
                                            });
                                            userRef.removeValue();
                                            userRef.goOffline();
                                            Toast.makeText(AccountActivity.this, "Account Deleted!", Toast.LENGTH_LONG);
                                        }
                                    });
                                } if(userInputDialogEditText.length() == 0) {
                                    Toast.makeText(AccountActivity.this, "Cannot Leave Password Field Empty!", Toast.LENGTH_LONG);
                                }
                            }
                        })

                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogBox, int id) {
                                        dialogBox.cancel();
                                    }
                                });

                AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
                alertDialogAndroid.show();
            }
        });
    }

    // Get the data from the database for the specific user, create new user object, display info.
    private void displayData(DataSnapshot dataSnapshot) {
        for(DataSnapshot s : dataSnapshot.getChildren()){
            User user = new User();

            // Make sure that the data exists on update
            if(s.child(userID).exists()){
                // Get the information from the snapshot and add it to the new User object.
                user.setName(s.child(userID).child("name").getValue().toString());
                user.setEmail(s.child(userID).child("email").getValue().toString());

                // Set UI text
                nameText.setText(user.getName());
                emailText.setText(user.getEmail());
            }
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
