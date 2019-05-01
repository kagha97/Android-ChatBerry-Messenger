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
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.bowfletchers.chatberry.Adapters.FriendStoriesAdapter;
import com.bowfletchers.chatberry.ClassLibrary.FirebaseInstances;
import com.bowfletchers.chatberry.R;
import com.bowfletchers.chatberry.ViewModel.FriendList.FriendListViewModel;
import com.bowfletchers.chatberry.ViewModel.UserData.UserStoryViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;

public class FriendStories extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    private RecyclerView friendStoryRecycler;
    private FriendStoriesAdapter friendStoriesAdapter;
    private List<com.bowfletchers.chatberry.ClassLibrary.UserStory> listFriendStories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_stories);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        referenceFirebaseInstances();

        setTitle("Friend's Stories");

        // init data
        friendStoryRecycler = findViewById(R.id.recyclerview_friendStories);
        friendStoryRecycler.setLayoutManager(new LinearLayoutManager(this));
        listFriendStories = new ArrayList<>();

        getFriendStoriesOfCurrentUser(currentUser.getUid());

        friendStoriesAdapter = new FriendStoriesAdapter(listFriendStories);
        friendStoryRecycler.setAdapter(friendStoriesAdapter);
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
               // Runtime.getRuntime().exit(0);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void getFriendStoriesOfCurrentUser(String userId) {
        // get all friends of current user
        // using friend list view model
        FriendListViewModel friendListViewModel = ViewModelProviders.of(this).get(FriendListViewModel.class);
        LiveData<DataSnapshot> friendListLiveData = friendListViewModel.getFriendListOfCurrentUser(userId);

        friendListLiveData.observe(this, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(@Nullable DataSnapshot dataSnapshot) {
                listFriendStories.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String currentFriendId = snapshot.child("friendId").getValue().toString();

                    // retrieve story for each friend id
                    // using user story view model
                    UserStoryViewModel userStoryViewModel =
                            ViewModelProviders.of(FriendStories.this).get(UserStoryViewModel.class);
                    LiveData<DataSnapshot> userStoryOfEachFriendLiveData =
                            userStoryViewModel.getUserStoryById(currentFriendId);

                    userStoryOfEachFriendLiveData.observe(FriendStories.this, new Observer<DataSnapshot>() {
                        @Override
                        public void onChanged(@Nullable DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() != null) {
                                com.bowfletchers.chatberry.ClassLibrary.UserStory currentFriendStory = dataSnapshot.getValue(com.bowfletchers.chatberry.ClassLibrary.UserStory.class);
                                listFriendStories.add(currentFriendStory);
                                friendStoriesAdapter.notifyDataSetChanged();
                            }
                        }
                    });
                }
            }
        });
    }

    private void referenceFirebaseInstances() {
        mAuth = FirebaseInstances.getDatabaseAuth();
        currentUser = mAuth.getCurrentUser();
    }
}
