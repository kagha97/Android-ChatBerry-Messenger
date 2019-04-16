package com.bowfletchers.chatberry.Activities;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bowfletchers.chatberry.R;
import com.bowfletchers.chatberry.ViewModel.FriendList.FriendListViewModel;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;

public class FriendProfile extends AppCompatActivity {

    private TextView textViewName;
    private TextView textViewEmail;
    private TextView textViewOnlineStatus;
    private ImageView imageViewPhoto;
    private Button buttonDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // extract friend id
        String friendId = getIntent().getStringExtra("friendId");
        referenceViews();
        getFriendData(friendId);

        buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goBackHome = new Intent(FriendProfile.this, FriendList.class);
                startActivity(goBackHome);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_only_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.homePageOnly:
                Intent intent = new Intent(this, ChatHistoryList.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                this.startActivity(intent);
                Runtime.getRuntime().exit(0);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getFriendData(String id) {
        FriendListViewModel friendModel = ViewModelProviders.of(this).get(FriendListViewModel.class);
        LiveData<DataSnapshot> friendLiveData = friendModel.getUserById(id);
        friendLiveData.observe(this, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(@Nullable DataSnapshot dataSnapshot) {
                String friendName = dataSnapshot.child("name").getValue().toString();
                String friendEmail = dataSnapshot.child("email").getValue().toString();
                String friendOnlineStatus = dataSnapshot.child("onlineStatus").getValue().toString();
                String friendPhotoURL = dataSnapshot.child("profilePicture").getValue().toString();

                setTitle(friendName);
                // Map data to views for friend profile
                textViewName.setText(friendName);
                textViewEmail.setText(friendEmail);
                if (friendOnlineStatus.equals("1")) {
                    textViewOnlineStatus.setText("Online");
                } else {
                    textViewOnlineStatus.setText("Offline");
                }
                Glide.with(FriendProfile.this).load(friendPhotoURL).placeholder(R.drawable.ic_person).into(imageViewPhoto);
            }
        });
    }

    private void referenceViews() {
        textViewName = findViewById(R.id.textViewFriendName);
        textViewEmail = findViewById(R.id.textViewEmail);
        textViewOnlineStatus = findViewById(R.id.textViewOnlineStatus);
        imageViewPhoto = findViewById(R.id.imageViewFriendPhoTo);
        buttonDone = findViewById(R.id.buttonDone);
    }
}
