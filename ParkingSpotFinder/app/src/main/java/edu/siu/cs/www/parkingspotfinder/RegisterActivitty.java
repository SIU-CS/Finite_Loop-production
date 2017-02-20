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

    private Button registerButton;
    private Button cancelButton;
    private DatabaseReference mDatabase;
    private EditText nameTextField, emailTextField, passwordTextField, verifyPasswordtextField;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private ProgressDialog progressDialog;

    private static final String TAG = "RegisterActivity:";

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
        nameTextField = (EditText) findViewById(R.id.nameText);
        passwordTextField = (EditText) findViewById(R.id.passwordText);
        verifyPasswordtextField = (EditText) findViewById(R.id.verifyPasswordText);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = emailTextField.getText().toString().trim();
                String name = nameTextField.getText().toString().trim();
                String password = passwordTextField.getText().toString().trim();
                String verifyPassword = verifyPasswordtextField.getText().toString().trim();

                mDatabase.setValue("Test");

                // Organize the user data
                HashMap<String, String> userDataMap = new HashMap<String, String>();
                userDataMap.put("Name", name);
                userDataMap.put("Email", email);
                userDataMap.put("Pass", password);

                if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(name)
                        && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(verifyPassword)){
                    if(password.equals(verifyPassword)){

                        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            FirebaseUser user = mAuth.getCurrentUser();

                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                // LOGGING
                                Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful() + " USER: "+user.getUid());
                                 progressDialog = ProgressDialog.show(RegisterActivitty.this, "Creating User", "Creating your account.", true);
                                if (!task.isSuccessful()) {
                                    Toast.makeText(RegisterActivitty.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                                Intent loginActivity = new Intent(RegisterActivitty.this, LoginActivity.class);
                                startActivity(loginActivity);
                                progressDialog.dismiss();
                                Toast.makeText(RegisterActivitty.this, "Successfully Registered!", Toast.LENGTH_LONG).show();

                                mDatabase.child(user.getUid()).setValue("test").addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Intent loginActivity = new Intent(RegisterActivitty.this, LoginActivity.class);
                                        startActivity(loginActivity);
                                        Toast.makeText(RegisterActivitty.this, "Successfully Registered!", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        });
                    } else {
                        Toast.makeText(RegisterActivitty.this, "Passwords do not match!", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(RegisterActivitty.this, "All fields are required!", Toast.LENGTH_LONG).show();
                }

                // Write changes to the database

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
}
