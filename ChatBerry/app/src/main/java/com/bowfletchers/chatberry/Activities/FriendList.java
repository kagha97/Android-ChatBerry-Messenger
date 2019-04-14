package com.bowfletchers.chatberry.Activities;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.print.PrinterId;
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
import android.view.View;
import android.widget.Toast;

import com.bowfletchers.chatberry.Adapters.FriendListAdapter;
import com.bowfletchers.chatberry.ClassLibrary.FirebaseInstances;
import com.bowfletchers.chatberry.ClassLibrary.Member;
import com.bowfletchers.chatberry.R;
import com.bowfletchers.chatberry.Testing.MockData;
import com.bowfletchers.chatberry.ViewModel.FriendList.FriendListViewModel;
import com.google.android.gms.dynamic.IFragmentWrapper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FriendList extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FriendListAdapter adapter;
    private List<Member> friendList = new ArrayList<>();

    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recyclerview_friendlist);
        mAuth = FirebaseInstances.getDatabaseAuth();
        usersRef = FirebaseInstances.getDatabaseReference("/users");
        FirebaseUser currentUser = mAuth.getCurrentUser();

        // Read friend list of current user and populate them on recycler view
        buildFriendList(currentUser.getUid());
        adapter = new FriendListAdapter(friendList);
        recyclerView.setLayoutManager(new LinearLayoutManager(FriendList.this));
        recyclerView.setAdapter(adapter);
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
                Intent userProfileIntent = new Intent(FriendList.this, UserProfile.class);
                startActivity(userProfileIntent);
                return true;
            case R.id.my_friends:
                Intent userFriendsIntent = new Intent(FriendList.this, FriendList.class);
                startActivity(userFriendsIntent);
                return true;
            case R.id.homePage:
                Intent chatListIntent = new Intent(FriendList.this, ChatHistoryList.class);
                startActivity(chatListIntent);
            case R.id.createStory:
                Intent createNewStoryIntent = new Intent(FriendList.this, CreateUserStory.class);
                startActivity(createNewStoryIntent);
            case R.id.friendStories:
                Intent friendStoriesIntent = new Intent(FriendList.this, FriendStories.class);
                startActivity(friendStoriesIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public  void buildFriendList(String memberId) {
        FriendListViewModel friendListViewModel = ViewModelProviders.of(this).get(FriendListViewModel.class);
        LiveData<DataSnapshot> friendListLiveData = friendListViewModel.getFriendListOfCurrentUser(memberId);

        friendListLiveData.observe(this, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(@Nullable DataSnapshot dataSnapshot) {
                friendList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // for each friend in friend list of current user
                    // get its id
                    String currentFriendId = snapshot.child("friendId").getValue().toString();

                    // get Friend data based on id
                    // and add it to a list
                    DatabaseReference currentFriendRef = usersRef.child(currentFriendId);
                    currentFriendRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            String userId = dataSnapshot.child("id").getValue().toString();
                            String userName = dataSnapshot.child("name").getValue().toString();
                            String userEmail = dataSnapshot.child("email").getValue().toString();
                            String userPhotoURL = dataSnapshot.child("profilePicture").getValue().toString();

                            Member currentFriend = new Member(userId, userName, userEmail, userPhotoURL);

                            friendList.add(currentFriend);
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });
    }

}
