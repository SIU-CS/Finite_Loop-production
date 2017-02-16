package edu.siu.cs.www.parkingspotfinder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private Button mLoginBtn;
    private EditText emailTextField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Create an instance of the database connection to firebase
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Create the needed elements for the activity
        mLoginBtn = (Button) findViewById(R.id.loginBtn);
        emailTextField = (EditText) findViewById(R.id.emailField);

        // Listen for login button click
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                // Get their name from the text field
                String email = emailTextField.getText().toString().trim();

                // Write changes to the database
                mDatabase.child("email").setValue(email);
            }
        });
    }
}
