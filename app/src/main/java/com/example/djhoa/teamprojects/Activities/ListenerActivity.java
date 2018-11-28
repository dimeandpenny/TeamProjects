package com.example.djhoa.teamprojects.Activities;

import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.djhoa.teamprojects.Components.Message;
import com.example.djhoa.teamprojects.R;
import com.example.djhoa.teamprojects.Components.SmsListener;

public class ListenerActivity extends AppCompatActivity {
    IntentFilter intentFilter;
    TextView senderBox;
    TextView contentBox;
    TextView latitudeBox;
    TextView longitudeBox;

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

        senderBox = findViewById(R.id.sender3);
        contentBox = findViewById(R.id.content);
        latitudeBox = findViewById(R.id.latitude);
        longitudeBox = findViewById(R.id.longitude);
        final Button nextBtn = findViewById(R.id.next);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Message next = smsListener.getNextMessage();
                if(next != null) {
                    try {
                        String[] parsed = next.getContent().split(" ");
                        Double latitude = Double.valueOf(parsed[0]);
                        Double longitude = Double.valueOf(parsed[1]);
                        String content = "";
                        for(int i=2; i<parsed.length; i++) {
                            content += parsed[i];
                            if( parsed.length - i != 1) {
                                content += " ";
                            }
                        }
                        latitudeBox.setText(String.valueOf(latitude));
                        longitudeBox.setText(String.valueOf(longitude));
                        senderBox.setText(next.getSender());
                        contentBox.setText(content);
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

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
