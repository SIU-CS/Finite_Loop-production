package edu.siu.cs.www.parkingspotfinder;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivitty extends AppCompatActivity {

    // UI elements
    private Button registerButton;
    private Button cancelButton;
    private DatabaseReference mDatabase;
    private EditText nameTextField, emailTextField, passwordTextField, verifyPasswordtextField;

    // Used for managing firebase connection
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    // Used to show how much progress has been made
    private ProgressDialog progressDialog;

    // Different strings that will need private classwide access
    private static final String TAG = "RegisterActivity:";
    private String email, password, name, verifyPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activitty);

        // Connect to Firebase
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

        // Create the elements for the view
        registerButton = (Button) findViewById(R.id.registerBtn);
        cancelButton = (Button) findViewById(R.id.cancelButton);
        emailTextField = (EditText) findViewById(R.id.emailText);
        nameTextField = (EditText) findViewById(R.id.nameInput);
        passwordTextField = (EditText) findViewById(R.id.passwordText);
        verifyPasswordtextField = (EditText) findViewById(R.id.verifyPasswordText);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = ProgressDialog.show(RegisterActivitty.this, "Creating User", "Creating your account.", true);

                // Get the information from the different fields
                email = getEmail(emailTextField.getText().toString().trim());
                name = getName(nameTextField.getText().toString().trim());
                password = passwordTextField.getText().toString().trim();
                verifyPassword = verifyPasswordtextField.getText().toString().trim();

                if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(name)
                        && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(verifyPassword)){
                    if(password.equals(verifyPassword)){

                        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // Organize the user data
                                HashMap<String, String> userDataMap = new HashMap<String, String>();
                                userDataMap.put("name", name);
                                userDataMap.put("email", email);

                                // LOGGING
//                                Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful() + " USER: "+user.getUid());

                                // If the tasks fail, notify the user
                                if (!task.isSuccessful()) {
                                    progressDialog.dismiss();
                                    Toast.makeText(RegisterActivitty.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    progressDialog.dismiss();

                                    // Write the neww user's information to the database
                                    mDatabase.child("users").child(user.getUid()).setValue(userDataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            // Start the login activity after writing user information to the database
                                            Intent mapActivity = new Intent(RegisterActivitty.this, MapActivity.class);
                                            startActivity(mapActivity);

                                            // Notify the user that they have been registered
                                            Toast.makeText(RegisterActivitty.this, "Successfully Registered!", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            }
                        });
                    } else {
                        Toast.makeText(RegisterActivitty.this, "Passwords do not match!", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(RegisterActivitty.this, "All fields are required!", Toast.LENGTH_LONG).show();
                }
            }
        });

        // Sets the click listener for the cancel button
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the login activity on cancel
                Intent loginActivity = new Intent(RegisterActivitty.this, LoginActivity.class);
                startActivity(loginActivity);
            }
        });
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
    public String getEmail(String email){
        return email;
    }
    public String getName(String name){
        return name;
    }
}
