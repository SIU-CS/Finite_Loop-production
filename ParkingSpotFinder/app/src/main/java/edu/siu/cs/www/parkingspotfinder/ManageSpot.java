package edu.siu.cs.www.parkingspotfinder;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.simplify.android.sdk.CardEditor;
import com.simplify.android.sdk.CardToken;
import com.simplify.android.sdk.Simplify;

public class ManageSpot extends AppCompatActivity {

    private final String SIMPLIFY_API_KEY = getResources().getString(R.string.simplify_api_key);

    private Button backArrowButton, pageInfoButton, addMoreTimeButton;

    private Simplify simplify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_spot_activity);

        backArrowButton = (Button) findViewById(R.id.backArrowButton);
        pageInfoButton = (Button) findViewById(R.id.pageInfoButton);
        addMoreTimeButton = (Button) findViewById(R.id.addMoreTimeButton);

        final CardEditor cardEditor = (CardEditor) findViewById(R.id.cardEditor);

        simplify = new Simplify();
        simplify.setApiKey(SIMPLIFY_API_KEY);

        // Listen for the changes in state of the card editor
        cardEditor.addOnStateChangedListener(new CardEditor.OnStateChangedListener() {
            @Override
            public void onStateChange(CardEditor cardEditor) {
                addMoreTimeButton.setEnabled(cardEditor.isValid());
            }
        });

        // Listen for when the user submits a new payment for processing
        addMoreTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simplify.createCardToken(cardEditor.getCard(), new CardToken.Callback(){
                    @Override
                    public void onSuccess(CardToken cardToken){
                        // Add POST to PPaaS here
                    }

                    @Override
                    public void onError(Throwable throwable){
                        // Handle errors here
                    }
                });
            }
        });


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
