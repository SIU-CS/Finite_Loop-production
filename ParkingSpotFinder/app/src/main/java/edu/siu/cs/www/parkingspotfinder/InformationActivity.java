package edu.siu.cs.www.parkingspotfinder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class InformationActivity extends AppCompatActivity {

    private Button backArrowButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        backArrowButton = (Button) findViewById(R.id.backArrowButton);

        backArrowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent menuActivityStart = new Intent(InformationActivity.this, MenuActivity.class);
                startActivity(menuActivityStart);
            }
        });
    }
}
