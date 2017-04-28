package edu.siu.cs.www.parkingspotfinder;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.simplify.android.sdk.CardEditor;
import com.simplify.android.sdk.CardToken;
import com.simplify.android.sdk.Simplify;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;

public class ManageSpot extends AppCompatActivity {

    //private final String SIMPLIFY_API_KEY = getResources().getString(R.string.simplify_api_key);
    private final String TAG = "MANAGE_SPOT_ACTIVITY::";
    private double rate;
    private final boolean DEBUG = true;

    private Button backArrowButton, pageInfoButton, addMoreTimeButton, leaveSpotButton;
    private Spinner hours, minutes;
    private TextView rateText;

    private ProgressDialog progressDialog;

    private Simplify simplify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_spot_activity);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String[] minuteItems = {"00","15","30"};
        String[] hourItems = {"00", "01", "02", "03", "04", "05"};

        ArrayAdapter<String> hoursList = new ArrayAdapter<String>
                (ManageSpot.this, android.R.layout.simple_spinner_dropdown_item, hourItems);
        ArrayAdapter<String> minutesList = new ArrayAdapter<String>
                (ManageSpot.this, android.R.layout.simple_spinner_dropdown_item, minuteItems);

        backArrowButton = (Button) findViewById(R.id.backArrowButton);
        pageInfoButton = (Button) findViewById(R.id.pageInfoButton);
        addMoreTimeButton = (Button) findViewById(R.id.addMoreTimeButton);
        leaveSpotButton = (Button) findViewById(R.id.leaveSpotButton);
        rateText = (TextView) findViewById(R.id.parkingRate);
        minutes = (Spinner) findViewById(R.id.minutesToAdd);
        hours = (Spinner) findViewById(R.id.hoursToAdd);

        minutes.setAdapter(minutesList);
        hours.setAdapter(hoursList);

        minutes.setSelection(1);
        hours.setSelection(1);

        addMoreTimeButton.setEnabled(false);

        final CardEditor cardEditor = (CardEditor) findViewById(R.id.cardEditor);

        simplify = new Simplify();
        simplify.setApiKey(getString(R.string.simplify_api_key));

        minutes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setRate();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });

        hours.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setRate();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });

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
                progressDialog = ProgressDialog.show(ManageSpot.this, "Processing", "Processing payment now.", true);
                simplify.createCardToken(cardEditor.getCard(), new CardToken.Callback(){
                    @Override
                    public void onSuccess(CardToken cardToken){
                        URL serviceURL = null;
                        HttpURLConnection con = null;
                        try{
                            // Configure connection to the PPaaS
                            serviceURL = new URL(getString(R.string.connection_string));
                            con = (HttpURLConnection) serviceURL.openConnection();

                            con.setRequestMethod("POST");
                            con.setRequestProperty("User-Agent", "Mozilla/5.0");
                            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

                            // This needs to be changed for when considering time and rate
                            String urlParams = "simplifyToken="+cardToken.getId()+"&amount="+
                                    getMoneyToSend();

                            con.setDoOutput(true);
                            DataOutputStream writeStream = new DataOutputStream(con.getOutputStream());
                            writeStream.writeBytes(urlParams);
                            writeStream.flush();
                            writeStream.close();

                            int respCode = con.getResponseCode();
                            System.out.println("\nSending 'POST' request to URL : " + serviceURL);
                            System.out.println("Post parameters : " + urlParams);
                            System.out.println("Response Code : " + respCode);

                            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                            String inputLine;
                            StringBuffer response = new StringBuffer();
                            while ((inputLine = in.readLine()) != null) {
                                response.append(inputLine);
                            }
                            in.close();
                            //print result
                            System.out.println(response.toString());
                            progressDialog.dismiss();
                            Toast.makeText(ManageSpot.this, "Payment Accepted!", Toast.LENGTH_LONG).show();

                            startActivity(new Intent(ManageSpot.this, MenuActivity.class));

                        } catch (MalformedURLException e){
                            Log.d(TAG, "MALORMED URL DETECTED");
                            e.printStackTrace();
                        } catch (IOException e){
                            Log.d(TAG, "IO EXCEPTION THROWN");
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable throwable){
                        // Handle errors here
                        Toast.makeText(ManageSpot.this, "Error: Could not process payment!", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        //notification to let user know there time is almost up
       /** NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle("Time Expiration")
                .setContentText("Your time is almost up"); **/


        pageInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ManageSpot.this)
                        .setTitle("Information")
                        .setMessage("Here you can see and add more time to your current time.  You can also leave the spot if you so choose.")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int leaveSpotButton) {
                                // Does nothing except close
                            }}).show();
            }
        });
        leaveSpotButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                startActivity (new Intent(ManageSpot.this, MapActivity.class));
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


    public void setRate(){
        Float minutesPer = Float.valueOf(minutes.getSelectedItem().toString())/60;
        Float hoursPer = Float.valueOf(hours.getSelectedItem().toString());
        rateText.setText(calculateRate(minutesPer, hoursPer));
    }

    public String calculateRate(float minutesRate, float hoursRate){
        DecimalFormat form = new DecimalFormat("#0.00");
        rate = (minutesRate + hoursRate);
        return form.format(rate);
    }

    public String getMoneyToSend(){
        Float minutesPer = Float.valueOf(minutes.getSelectedItem().toString())/60;
        Float hoursPer = Float.valueOf(hours.getSelectedItem().toString());

        String rateToSend = calculateRate(minutesPer, hoursPer);
        rateToSend = rateToSend.replaceAll("\\.","");

        if(DEBUG)
            Log.d(TAG, "MONEY_TO_SEND::"+rateToSend);

        return rateToSend;
    }
}
