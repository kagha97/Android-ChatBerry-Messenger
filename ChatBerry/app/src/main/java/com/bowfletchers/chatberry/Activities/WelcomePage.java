package com.bowfletchers.chatberry.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.bowfletchers.chatberry.R;

public class WelcomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void singIn(View view) {
        // navigate to sign in activity
        Intent signInIntent = new Intent(WelcomePage.this, Login_account.class);
        startActivity(signInIntent);
    }

    public void singUp(View view) {
        // navigate to register acc activity
        Intent signUpIntent = new Intent(WelcomePage.this, Register_account.class);
        startActivity(signUpIntent);
    }
}
