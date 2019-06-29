package com.example.whatsappclone;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseInstallation;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Save the current Installation to Back4App
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }
}
