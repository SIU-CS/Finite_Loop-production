package edu.siu.cs.www.parkingspotfinder;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import static java.lang.String.valueOf;

public class InitialPaymentActivity extends AppCompatActivity {

    private final String TAG = "PAYMENT_ACTIVITY::";
    private double rate;
    private final boolean DEBUG = true;
    private Button backArrowButton, pageInfoButton, purchaseButton;
    private TextView parkingRate;
    private Spinner hours, minutes;

    private ProgressDialog progressDialog;

    private Simplify simplify;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_payment);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String[] minuteOptions = {"00","15","30"};
        String[] hourOptions = {"00", "01", "02", "03", "04", "05"};

        ArrayAdapter<String> hoursList = new ArrayAdapter<String>
                (InitialPaymentActivity.this, android.R.layout.simple_spinner_dropdown_item, hourOptions);
        ArrayAdapter<String> minutesList = new ArrayAdapter<String>
                (InitialPaymentActivity.this, android.R.layout.simple_spinner_dropdown_item, minuteOptions);


        backArrowButton = (Button) findViewById(R.id.backArrowButton);
        pageInfoButton = (Button) findViewById(R.id.pageInfoButton);
        purchaseButton = (Button) findViewById(R.id.purchaseButton);
        parkingRate = (TextView) findViewById(R.id.parkingRate);
        minutes = (Spinner) findViewById(R.id.minutesSpinner);
        hours = (Spinner) findViewById(R.id.hoursSpinner);

        minutes.setAdapter(minutesList);
        hours.setAdapter(hoursList);

        minutes.setSelection(1);
        hours.setSelection(1);

        purchaseButton.setEnabled(false);

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
                purchaseButton.setEnabled(cardEditor.isValid());
            }
        });

        purchaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = ProgressDialog.show(InitialPaymentActivity.this, "Processing", "Processing payment now.", true);
                simplify.createCardToken(cardEditor.getCard(), new CardToken.Callback() {
                    @Override
                    public void onSuccess(CardToken cardToken) {
                        URL serviceURL = null;
                        HttpURLConnection con = null;
                        try {
                            // Configure connection to the PPaaS
                            serviceURL = new URL(getString(R.string.connection_string));
                            con = (HttpURLConnection) serviceURL.openConnection();

                            con.setRequestMethod("POST");
                            con.setRequestProperty("User-Agent", "Mozilla/5.0");
                            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

                            // This needs to be changed for when considering time and rate
                            String urlParams = "simplifyToken=" + cardToken.getId() + "&amount=" +
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
                            Toast.makeText(InitialPaymentActivity.this, "Payment Accepted!", Toast.LENGTH_LONG).show();

                            startActivity(new Intent(InitialPaymentActivity.this, MenuActivity.class));

                        } catch (MalformedURLException e) {
                            Log.d(TAG, "MALFORMED URL DETECTED");
                            e.printStackTrace();
                        } catch (IOException e) {
                            Log.d(TAG, "IO EXCEPTION THROWN");
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        // Handle errors here
                        Toast.makeText(InitialPaymentActivity.this, "Error: Could not process payment!", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        pageInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(InitialPaymentActivity.this)
                        .setTitle("Information")
                        .setMessage("Here you can purchase the amount of time in the parking spot of your choice.")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                            }
                        }).show();
            }
        });

        backArrowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InitialPaymentActivity.this, MapActivity.class));
            }
        });
    }

    public void setRate() {

        Double minutesPer = Double.valueOf(minutes.getSelectedItem().toString())/60;
        Double hoursPer = Double.valueOf(hours.getSelectedItem().toString());

        DecimalFormat form = new DecimalFormat("#0.00");

        rate = (minutesPer + hoursPer);

        Log.d(TAG, "RATE::" + form.format(rate));

        parkingRate.setText(form.format(rate));
    }

//    public void setTime() {
//        int hrs = (Integer.valueOf(hours.getSelectedItem().toString()));
//        int mins = (Integer.valueOf(minutes.getSelectedItem().toString()));
//
//        Calendar cal = Calendar.getInstance(Locale.US);
//
//        String currentTimeString = String.format("%1$tH:%1$tM",cal);
//        int hoursInClock = cal.get(Calendar.HOUR_OF_DAY);
//        int minutesInClock = cal.get(Calendar.MINUTE);
//        cal.add(Calendar.MINUTE, (minutesInClock + mins));
//        cal.add(Calendar.HOUR_OF_DAY, (hoursInClock + hrs));
//        String newTimeString = String.format("%1$tH:%1$tM",cal);
//    }

    public String getMoneyToSend() {
        Double minutesPer = Double.valueOf(minutes.getSelectedItem().toString())/60;
        Double hoursPer = Double.valueOf(hours.getSelectedItem().toString());

        DecimalFormat form = new DecimalFormat("#0.00");

        rate = (minutesPer + hoursPer);

        String rateToSend = form.format(rate);
        rateToSend = rateToSend.replaceAll("\\.", "");

        if (DEBUG)
            Log.d(TAG, "MONEY_TO_SEND::" + rateToSend);

        return rateToSend;
    }
}
