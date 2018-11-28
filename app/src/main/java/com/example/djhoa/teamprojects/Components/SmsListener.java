package com.example.djhoa.teamprojects.Components;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsMessage;

import java.util.LinkedList;
import java.util.Queue;

public class SmsListener extends BroadcastReceiver {

    private SharedPreferences preferences;
    public Queue<Message> messages;
    private Context context;

    public SmsListener() {
        messages = new LinkedList<>();
    }

    public SmsListener(Context context) {
        this.messages = new LinkedList<>();
        this.context = context;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
            Bundle bundle = intent.getExtras();           //---get the SMS message passed in---
            SmsMessage[] msgs;
            if (bundle != null){
                //---retrieve the SMS message received---
                try{
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    msgs = new SmsMessage[pdus.length];
                    for(int i=0; i<msgs.length; i++){
                        msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                        Message message = new Message();
                        message.setContent(msgs[i].getMessageBody());
                        message.setSender(msgs[i].getOriginatingAddress());
                        messages.add(message);
                        if(context != null) {
                            message.print(context);
                        }
                    }

                }catch(Exception e){
                    System.out.println("Error: Could not process incoming request.");
                }
            }
        }
    }

    public Message getNextMessage() {
        if(messages.size() > 0) {
            return messages.remove();
        } else {
            return null;
        }
    }
}