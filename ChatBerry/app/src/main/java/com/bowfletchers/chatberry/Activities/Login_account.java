package com.bowfletchers.chatberry.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.bowfletchers.chatberry.R;

public class Login_account extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_account);
    }

    public void confirmSingIn(View view) {
        // navigate user to the chat history
        Intent goToIntent = new Intent(Login_account.this, Friend_List.class);
        startActivity(goToIntent);
    }

    public void cancelSignIn(View view) {
        // navigate user to the welcome page
        Intent backToWelcomePage = new Intent(Login_account.this, WelcomePage.class);
        startActivity(backToWelcomePage);
    }
}
