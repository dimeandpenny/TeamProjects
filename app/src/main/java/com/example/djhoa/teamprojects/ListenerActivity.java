package com.example.djhoa.teamprojects;

import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ListenerActivity extends AppCompatActivity {
    IntentFilter intentFilter;
    TextView senderBox;
    TextView contentBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_listener);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




        //SMS event receiver
        final SmsListener smsListener = new SmsListener(getApplicationContext());
        intentFilter = new IntentFilter();
        intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(smsListener, intentFilter);

        senderBox = findViewById(R.id.sender);
        contentBox = findViewById(R.id.content);
        final Button nextBtn = findViewById(R.id.next);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Message next = smsListener.getNextMessage();
                if(next != null) {
                    senderBox.setText(next.getSender());
                    contentBox.setText(next.getContent());
                } else {
                    Toast.makeText(getApplicationContext(), "No messages in queue", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
