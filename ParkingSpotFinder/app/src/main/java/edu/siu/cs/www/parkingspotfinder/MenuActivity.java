package edu.siu.cs.www.parkingspotfinder;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MenuActivity extends AppCompatActivity {

    private Button backArrow, accountButton, informationButton, logOutButton;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        backArrow = (Button) findViewById(R.id.backArrowButton);
        accountButton = (Button) findViewById(R.id.accountButton);
        informationButton = (Button) findViewById(R.id.informationButton);
        logOutButton = (Button) findViewById(R.id.logOutButton);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

            }
        };

        // Set the back arrow to go back to the map activity
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapActivityStart = new Intent(MenuActivity.this, MapActivity.class);
                startActivity(mapActivityStart);
            }
        });

        // Change to the account page on click
        accountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent accountActivityStart = new Intent(MenuActivity.this, AccountActivity.class);
                startActivity(accountActivityStart);
            }
        });

        // Change to the information page on a click
        informationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent informationActivityStart = new Intent(MenuActivity.this, InformationActivity.class);
                startActivity(informationActivityStart);
            }
        });

        // Logout of the application on click
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MenuActivity.this)
                        .setTitle("Confirmation")
                        .setMessage("Do you really want to logout?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                mAuth.signOut();
                                Intent loginActivityStart = new Intent(MenuActivity.this, LoginActivity.class);
                                startActivity(loginActivityStart);
                                Toast.makeText(MenuActivity.this, "Logged Out Successfully", Toast.LENGTH_SHORT).show();
                            }})
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });
    }
}
