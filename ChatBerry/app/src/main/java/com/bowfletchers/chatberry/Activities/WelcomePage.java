package com.bowfletchers.chatberry.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.provider.FontRequest;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bowfletchers.chatberry.ClassLibrary.Chat;
import com.bowfletchers.chatberry.ClassLibrary.Member;
import com.bowfletchers.chatberry.ClassLibrary.Message;
import com.bowfletchers.chatberry.R;
import com.bowfletchers.chatberry.Testing.MockData;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
