package com.bowfletchers.chatberry;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Register_account extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_account);
    }

    public void cancelRegister(View view) {
        // navigate back to welcome page
        Intent backToSignInIntent = new Intent(Register_account.this, WelcomePage.class);
        startActivity(backToSignInIntent);
    }

    public void confirmRegister(View view) {
        // navigate to the chat history page
    }
}
