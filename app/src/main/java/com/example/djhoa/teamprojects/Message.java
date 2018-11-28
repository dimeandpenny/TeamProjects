package com.example.djhoa.teamprojects;

import android.content.Context;
import android.widget.Toast;

public class Message {

    private String sender;

    private String content;

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void print(Context context) {
        CharSequence text = "Sender: " + sender + ", Content: " + content;
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

}
