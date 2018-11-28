package com.example.djhoa.teamprojects.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.djhoa.teamprojects.R;

public class RegisterActivity extends AppCompatActivity {
    Button registerBtn;
    EditText nameTxt;
    EditText numberTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        nameTxt = findViewById(R.id.name);
        numberTxt = findViewById(R.id.number);
        registerBtn = findViewById(R.id.register);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                mainView();
            }
        });



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    public void mainView() {
        Intent intent = new Intent();
        intent.putExtra("name", nameTxt.getText().toString());
        intent.putExtra("number", numberTxt.getText().toString());
        setResult(RESULT_OK, intent);
        finish();
    }
}
