package com.bowfletchers.chatberry.Activities;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.bowfletchers.chatberry.Adapters.AvailableUsersInfoAdapter;
import com.bowfletchers.chatberry.ClassLibrary.FirebaseInstances;
import com.bowfletchers.chatberry.ClassLibrary.Member;
import com.bowfletchers.chatberry.R;
import com.bowfletchers.chatberry.ViewModel.AvailableUsersModel.GetAvailableUsersReference;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;

public class AvailableUsers extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private AvailableUsersInfoAdapter mAdapter;
    private ArrayList<Member> userNames = new ArrayList<>();
    private ArrayList<String> myFriendsIdList = new ArrayList<>();
    private ArrayList<String> myInvitationsList = new ArrayList<>();
    private final String auth = FirebaseAuth.getInstance().getUid();

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
        GetAvailableUsersReference invitationsViewModel = ViewModelProviders.of(this).get(GetAvailableUsersReference.class);
        LiveData<DataSnapshot> invitations = invitationsViewModel.getInvitations();
        invitations.observe(this, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(@Nullable DataSnapshot dataSnapshot) {
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
        });
    }

    private void getMyFriends() {
        GetAvailableUsersReference friendsViewModel = ViewModelProviders.of(this).get(GetAvailableUsersReference.class);
        LiveData<DataSnapshot> friends = friendsViewModel.getFriends();
        friends.observe(this, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(@Nullable DataSnapshot dataSnapshot) {
                myFriendsIdList.clear();
                for(DataSnapshot myFriends: dataSnapshot.getChildren()){
                    myFriendsIdList.add(myFriends.child("friendId").getValue().toString());
                }
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    public void getAllAvailableUsers()
    {
        GetAvailableUsersReference userViewModel = ViewModelProviders.of(this).get(GetAvailableUsersReference.class);
        LiveData<DataSnapshot> users = userViewModel.getUsers();
        users.observe(this, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(@Nullable DataSnapshot dataSnapshot) {
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
                mAdapter.notifyDataSetChanged();
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

            case R.id.newgc:
                Intent newGC = new Intent(AvailableUsers.this, NewGroupChat.class);
                startActivity(newGC);
                return true;

            case R.id.createStory:
                Intent createNewStoryIntent = new Intent(AvailableUsers.this, UserStory.class);
                startActivity(createNewStoryIntent);
            case R.id.friendStories:
                Intent friendStoriesIntent = new Intent(AvailableUsers.this, FriendStories.class);
                startActivity(friendStoriesIntent);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
}
