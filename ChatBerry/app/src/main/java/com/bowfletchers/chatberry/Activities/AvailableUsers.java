package com.bowfletchers.chatberry.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.bowfletchers.chatberry.Adapters.AvailableUsersInfoAdapter;
import com.bowfletchers.chatberry.ClassLibrary.FirebaseInstances;
import com.bowfletchers.chatberry.ClassLibrary.Member;
import com.bowfletchers.chatberry.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AvailableUsers extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private AvailableUsersInfoAdapter mAdapter;
    private ArrayList<Member> userNames = new ArrayList<>();
    private ArrayList<String> myFriendsIdList = new ArrayList<>();
    private ArrayList<String> myInvitationsList = new ArrayList<>();
    private final String auth = FirebaseAuth.getInstance().getUid();
    //private final DatabaseReference myFriends = FirebaseDatabase.getInstance().getReference("/users/" + auth + "/friends" );

    FirebaseAuth mAuth;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_users);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseInstances.getDatabaseAuth();
        mUser = mAuth.getCurrentUser();
        if (mUser == null) {
            Intent backToSignInIntent = new Intent(AvailableUsers.this, LoginAccount.class);
            startActivity(backToSignInIntent);
        }
        getMyFriends();
        getMyInvitations();
        getAllAvailableUsers();
        mRecyclerView = findViewById(R.id.available_users_recycler_view);
        mAdapter = new AvailableUsersInfoAdapter(userNames, this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void getMyInvitations() {
        final DatabaseReference myInvitations = FirebaseDatabase.getInstance().getReference("invitations");
        myInvitations.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myInvitationsList.clear();
                for(DataSnapshot invitations: dataSnapshot.getChildren()){
                    if(invitations.child("senderId").getValue().toString().equals(auth)){
                        myInvitationsList.add(invitations.child("receiverId").getValue().toString());
                    }
                    else if(invitations.child("receiverId").getValue().toString().equals(auth)){
                        myInvitationsList.add(invitations.child("senderId").getValue().toString());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getMyFriends() {
        DatabaseReference users = FirebaseDatabase.getInstance().getReference("users");
        DatabaseReference friends = users.child(auth);
        DatabaseReference friendList = friends.child("friends");
        friendList.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myFriendsIdList.clear();
                for(DataSnapshot myFriends: dataSnapshot.getChildren()){
                    myFriendsIdList.add(myFriends.child("friendId").getValue().toString());
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getAllAvailableUsers()
    {
        DatabaseReference usersReference = FirebaseInstances.getDatabaseReference("users");
        usersReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userNames.clear();
                for(DataSnapshot users : dataSnapshot.getChildren())
                {
                    if(FirebaseAuth.getInstance().getUid().equals(users.child("id").getValue().toString())) {
                        userNames.add(new Member(users.child("id").getValue().toString(),users.child("name").getValue().toString(), users.child("email").getValue().toString(), users.child("profilePicture").getValue().toString(),  false , true));
                    }
                    else if(myFriendsIdList.contains(users.child("id").getValue().toString())){
                        Log.d("Friends", "True");
                        userNames.add(new Member(users.child("id").getValue().toString(),users.child("name").getValue().toString(), users.child("email").getValue().toString(), users.child("profilePicture").getValue().toString(),  false , true));
                    }
                    else {
                        userNames.add(new Member(users.child("id").getValue().toString(), users.child("name").getValue().toString(), users.child("email").getValue().toString(), users.child("profilePicture").getValue().toString()));
                    }
                }
                //Toast.makeText(AvailableUsers.this, userNames.get(1), Toast.LENGTH_LONG).show();
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.my_profile:
                Intent userProfileIntent = new Intent(AvailableUsers.this, UserProfile.class);
                startActivity(userProfileIntent);
                return true;
            case R.id.my_friends:
                Intent userFriendsIntent = new Intent(AvailableUsers.this, FriendList.class);
                startActivity(userFriendsIntent);
                return true;
            case R.id.my_friend_requests:
                Intent friendRequests = new Intent(AvailableUsers.this, FriendRequests.class);
                startActivity(friendRequests);
                return true;
            case R.id.homePage:
                Intent chatListIntent = new Intent(AvailableUsers.this, ChatHistoryList.class);
                startActivity(chatListIntent);
        }
        return super.onOptionsItemSelected(item);
    }
}
