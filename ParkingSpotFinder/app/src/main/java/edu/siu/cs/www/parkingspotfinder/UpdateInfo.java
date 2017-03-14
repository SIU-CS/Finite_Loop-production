package edu.siu.cs.www.parkingspotfinder;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.app.AlertDialog.Builder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UpdateInfo extends AppCompatActivity {
    private Button backButton;
    private Button cancelButton;
    private Button resetPasswordButton;
    private Button saveUpdatedInfoButton;
    private Button pageInfoButton;

    private EditText userName;
    private EditText userEmail;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference ref;
    private DatabaseReference userRef;

    private String name, email, userID;

    private final String TAG = "UPDATE_INFO_ACT::";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_info);

        backButton = (Button) findViewById(R.id.backButton);
        cancelButton = (Button) findViewById(R.id.cancelButton);
        resetPasswordButton = (Button) findViewById(R.id.changePasswordButton);
        saveUpdatedInfoButton = (Button) findViewById(R.id.updateButton);
        pageInfoButton = (Button) findViewById(R.id.pageInfoButton);

        userEmail = (EditText) findViewById(R.id.emailInput);
        userName = (EditText) findViewById(R.id.nameInput);

        name = userName.getText().toString();
        email = userEmail.getText().toString();

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        userID = mUser.getUid();
        ref = FirebaseDatabase.getInstance().getReference();
        userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userID);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mAuth.getCurrentUser();
                if(user != null){
                    Log.d(TAG, "signed_in");
                } else {
                    Log.d(TAG, "signed_out");
                }
            }
        };

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        pageInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(UpdateInfo.this)
                        .setTitle("Information")
                        .setMessage("Here you can edit your name and email.  " +
                                "You can also send a password reset email by clicking the change " +
                                "password button.  Click update info to save the updated information " +
                                "and cancel to quit.")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // Does nothing except close
                            }}).show();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(UpdateInfo.this)
                        .setTitle("Confirmation")
                        .setMessage("Do you wish to cancel updating your information?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Intent loginActivityStart = new Intent(UpdateInfo.this, AccountActivity.class);
                                startActivity(loginActivityStart);
                                Toast.makeText(UpdateInfo.this, "Cancelled Information Update.", Toast.LENGTH_SHORT).show();
                            }})
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent menuActivityStart = new Intent(UpdateInfo.this, AccountActivity.class);
                startActivity(menuActivityStart);
            }
        });

        saveUpdatedInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(UpdateInfo.this)
                        .setTitle("Confirmation")
                        .setMessage("Do you wish to continue updating your information?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                if(!name.matches("") || !email.matches("")){
                                    Toast.makeText(UpdateInfo.this, "Information Updated", Toast.LENGTH_SHORT).show();
                                }
                            }})
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });

        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
                new AlertDialog.Builder(UpdateInfo.this)
                        .setTitle("Confirmation")
                        .setMessage("Do you wish to continue sending the password reset email to "+mUser.getEmail()+"?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
                                mAuth.sendPasswordResetEmail(mUser.getEmail())
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    Log.d(TAG, "Email Sent");
                                                } else {
                                                    Log.d(TAG, "Failed to Send Email");
                                                }
                                            }
                                        });
                                Toast.makeText(UpdateInfo.this, "Password reset email has been sent!", Toast.LENGTH_LONG);
                            }})
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });
    }
}
