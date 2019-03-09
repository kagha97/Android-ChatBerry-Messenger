package com.bowfletchers.chatberry;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Login_account extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_account);
    }

    public void confirmSingIn(View view) {
        // navigate user to the chat history
    }

    public void cancelSignIn(View view) {
        // navigate user to the welcome page
        Intent backToWelcomePage = new Intent(Login_account.this, WelcomePage.class);
        startActivity(backToWelcomePage);
    }
}
