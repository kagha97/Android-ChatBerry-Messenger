package com.bowfletchers.chatberry.Activities;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.bowfletchers.chatberry.Adapters.AvailableUsersInfoAdapter;
import com.bowfletchers.chatberry.Adapters.FriendRequestAdapter;
import com.bowfletchers.chatberry.ClassLibrary.Member;
import com.bowfletchers.chatberry.R;
import com.bowfletchers.chatberry.ViewModel.AvailableUsersModel.GetAvailableUsersReference;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FriendRequests extends AppCompatActivity {

    private String userId = FirebaseAuth.getInstance().getUid();
    public List<Member> requesteeList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private FriendRequestAdapter mAdapter;
    private String invitationId;
    private List<String> invitationList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_requests);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Friend Requests");
        getAllFriendRequests();
        mRecyclerView = findViewById(R.id.requestee_recycler_view);
        mAdapter = new FriendRequestAdapter(this, requesteeList, invitationList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void getAllFriendRequests() {
        final GetAvailableUsersReference invitationsViewModel = ViewModelProviders.of(this).get(GetAvailableUsersReference.class);
        LiveData<DataSnapshot> invitations = invitationsViewModel.getInvitations();
        invitations.observe(this, new Observer<DataSnapshot>() {
                    @Override
                    public void onChanged(@Nullable DataSnapshot dataSnapshot) {
                        invitationList.clear();
                        requesteeList.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            final String receiver = snapshot.child("receiverId").getValue().toString();
                            final String sender = snapshot.child("senderId").getValue().toString();
                            if (receiver.equals(userId)) {
                                invitationId = snapshot.getKey();
                                LiveData<DataSnapshot> users = invitationsViewModel.getUsers();
                                users.observe(FriendRequests.this, new Observer<DataSnapshot>() {
                                    @Override
                                    public void onChanged(@Nullable DataSnapshot dataSnapshot) {
                                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                            if (userSnapshot.child("id").getValue().toString().equals(sender)) {
                                                Log.d("Name", userSnapshot.child("name").getValue().toString());
                                                invitationList.add(invitationId);
                                                requesteeList.add(new Member(userSnapshot.child("id").getValue().toString(), userSnapshot.child("name").getValue().toString(), userSnapshot.child("email").getValue().toString(), userSnapshot.child("profilePicture").getValue().toString()));
                                            }
                                        }
                                        mAdapter.notifyDataSetChanged();
                                    }
                                });
                            }
                        }
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
                Intent userProfileIntent = new Intent(FriendRequests.this, UserProfile.class);
                startActivity(userProfileIntent);
                return true;

            case R.id.my_friends:
                Intent userFriendsIntent = new Intent(FriendRequests.this, FriendList.class);
                startActivity(userFriendsIntent);
                return true;

            case R.id.homePage:
                Intent chatListIntent = new Intent(FriendRequests.this, ChatHistoryList.class);
                startActivity(chatListIntent);
                return true;

            case R.id.createStory:
                Intent createNewStoryIntent = new Intent(FriendRequests.this, UserStory.class);
                startActivity(createNewStoryIntent);
                return true;

            case R.id.friendStories:
                Intent friendStoriesIntent = new Intent(FriendRequests.this, FriendStories.class);
                startActivity(friendStoriesIntent);
                return true;

            case R.id.my_friend_requests:
                Intent friendRequestIntent = new Intent(FriendRequests.this, FriendRequests.class);
                startActivity(friendRequestIntent);
                return true;

            case R.id.newgc:
                Intent newGC = new Intent(FriendRequests.this, NewGroupChat.class);
                startActivity(newGC);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
