package edu.siu.cs.www.parkingspotfinder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class SelectSpotActivity extends AppCompatActivity {

    private TextView tagView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_spot);

        tagView = (TextView) findViewById(R.id.test);

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            String tag = extras.getString("lot-tag");
            tagView.setText(tag);
        }
    }
}
