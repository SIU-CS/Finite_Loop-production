package edu.siu.cs.www.parkingspotfinder;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ManageSpot extends AppCompatActivity {

    private Button backArrowButton, pageInfoButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_spot_activity);

        backArrowButton = (Button) findViewById(R.id.backArrowButton);
        pageInfoButton = (Button) findViewById(R.id.pageInfoButton);

        pageInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ManageSpot.this)
                        .setTitle("Information")
                        .setMessage("Here you can see and add more time to your current time.  You can also leave the spot if you so choose.")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // Does nothing except close
                            }}).show();
            }
        });

        // Start the menu again if the user decides to go back
        backArrowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ManageSpot.this, MenuActivity.class));
            }
        });
    }
}
