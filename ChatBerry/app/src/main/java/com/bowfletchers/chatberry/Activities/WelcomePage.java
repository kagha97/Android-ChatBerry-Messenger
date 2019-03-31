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

import com.bowfletchers.chatberry.R;
import com.bowfletchers.chatberry.ViewModel.ChatBerryViewModel;
import com.google.firebase.database.DataSnapshot;

public class WelcomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // test Firebase connect
        ChatBerryViewModel viewModel = ViewModelProviders.of(this).get(ChatBerryViewModel.class);
        LiveData<DataSnapshot> liveData = viewModel.getDataSnapshotLiveData();

        liveData.observe(this, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(@Nullable DataSnapshot dataSnapshot) {
                //Toast.makeText(WelcomePage.this, dataSnapshot.getValue().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void singIn(View view) {
        // navigate to sign in activity
        Intent signInIntent = new Intent(WelcomePage.this, LoginAccount.class);
        startActivity(signInIntent);
    }

    public void singUp(View view) {
        // navigate to register acc activity
        Intent signUpIntent = new Intent(WelcomePage.this, RegisterAccount.class);
        startActivity(signUpIntent);
    }
}
