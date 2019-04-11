package com.bowfletchers.chatberry.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.bowfletchers.chatberry.Adapters.FriendListAdapter;
import com.bowfletchers.chatberry.ClassLibrary.Member;
import com.bowfletchers.chatberry.R;
import com.bowfletchers.chatberry.Testing.MockData;

import java.util.List;

public class FriendList extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FriendListAdapter adapter;
    private List<Member> friendList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Member testUser = MockData.buildUserObject();
        recyclerView = findViewById(R.id.recyclerview_friendlist);
        friendList = testUser.getFriendList();
        adapter = new FriendListAdapter(friendList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    public void viewMessage(View view) {
        Intent goToIntent = new Intent(this, MessageViewer.class);
        startActivity(goToIntent);
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
        }
        return super.onOptionsItemSelected(item);
    }
}
