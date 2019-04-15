package com.bowfletchers.chatberry.Activities;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.bowfletchers.chatberry.ClassLibrary.FirebaseInstances;
import com.bowfletchers.chatberry.R;
import com.bowfletchers.chatberry.ViewModel.ChatBerryViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;

public class WelcomePage extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseInstances.getDatabaseAuth();
        mUser = mAuth.getCurrentUser();

        if (mUser != null) {
            Intent gotoChatlistIntent = new Intent(WelcomePage.this, AvailableUsers.class);
            startActivity(gotoChatlistIntent);
            finish();
        }
    }

    public void singIn(View view) {
        // navigate to sign in activity
        Intent signInIntent = new Intent(WelcomePage.this, LoginAccount.class);
        startActivity(signInIntent);
        finish();
    }

    public void singUp(View view) {
        // navigate to register acc activity
        Intent signUpIntent = new Intent(WelcomePage.this, RegisterAccount.class);
        startActivity(signUpIntent);
    }
}