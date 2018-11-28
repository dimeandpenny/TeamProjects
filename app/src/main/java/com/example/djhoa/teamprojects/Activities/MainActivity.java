package com.example.djhoa.teamprojects.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.Manifest;
import android.content.pm.PackageManager;

import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.djhoa.teamprojects.R;
import com.example.djhoa.teamprojects.Components.SmsListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0 ;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 0;

    Button sendBtn, registerBtn;
    EditText txtphoneNo, txtMessage;
    String phoneNo, message;
    SmsListener smsListener;
    IntentFilter intentFilter;
    Map<String, String> numbersByUser;
    ArrayList<String> users;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialize containers
        users = new ArrayList<>();
        numbersByUser = new HashMap<>();

        // On-screen components
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendSMSMessage();
            }
        });

        sendBtn = findViewById(R.id.btnSendSMS);
        registerBtn = findViewById(R.id.register);
        txtMessage = findViewById(R.id.editText2);
        txtphoneNo = findViewById(R.id.editText);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                listenerView();
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                registerView();
            }
        });
        spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                txtphoneNo.setText(numbersByUser.get(users.get(position)), TextView.BufferType.EDITABLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do Nothing
            }

        });
        refreshSpinner();

        //SMS event receiver
        smsListener = new SmsListener(getApplicationContext());
        intentFilter = new IntentFilter();
        intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(smsListener, intentFilter);
    }

    public void refreshSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<> (this,android.R.layout.simple_spinner_dropdown_item, users);
        spinner.setAdapter(adapter);
    }

    protected void sendSMSMessage() {
        if(txtMessage == null || txtphoneNo == null) {
            return;
        }
        phoneNo = txtphoneNo.getText().toString();
        message = txtMessage.getText().toString();
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        } else {
            SmsManager smsManager = SmsManager.getDefault();
            Toast.makeText(getApplicationContext(),
                    "Success: \"" + txtMessage.getText().toString() + "\" requested", Toast.LENGTH_LONG).show();
            try {
                LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

                Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                double longitude = location.getLongitude();
                double latitude = location.getLatitude();
                message = String.valueOf(latitude) + " " + String.valueOf(longitude) + " " + txtMessage.getText().toString();
                smsManager.sendTextMessage(phoneNo, null, message, null, null);
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    public void listenerView() {
        Intent intent = new Intent(this, ListenerActivity.class);
//        EditText editText = (EditText) findViewById(R.id.editText);
//        String message = editText.getText().toString();
//        intent.putExtra("Wowdy!", message);
        startActivity(intent);
    }

    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        Toast.makeText(getApplicationContext(), "Test", Toast.LENGTH_LONG).show();
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneNo, null, message, null, null);
                    Toast.makeText(getApplicationContext(), "SMS sent.",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "SMS failed, please try again.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }

    }

    public void registerView() {
        Intent intent = new Intent(this, RegisterActivity.class);
        int requestCode = 420;
        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        // if no result, return
        if(resultCode != -1) {
            return;
        }
        String name = data.getStringExtra("name");
        String number = data.getStringExtra("number");
        numbersByUser.put(name, number);
        if(!users.contains(name)) {
            users.add(name);
        }
        refreshSpinner();
        Toast.makeText(getApplicationContext(), "User \"" + name + "\" registered", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


//    ActivityCompat.requestPermissions(this,
//            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//            MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
}
