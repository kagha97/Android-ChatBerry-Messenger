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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.bowfletchers.chatberry.Adapters.AvailableUsersInfoAdapter;
import com.bowfletchers.chatberry.Adapters.GroupChatsListAdapter;
import com.bowfletchers.chatberry.R;
import com.bowfletchers.chatberry.ViewModel.GroupChatsModel.GetGroupChatsReference;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;

public class GroupHistoryList extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private GroupChatsListAdapter mAdapter;
    private String groupId;
    private String groupName;
    private List<String> finalGroupsNameList = new ArrayList<>();
    private List<String> ownerGroupNamesList = new ArrayList<>();
    private List<String> finalGroupIdList = new ArrayList<>();
    private List<String> ownergroupIdList = new ArrayList<>();
    private final String auth = FirebaseAuth.getInstance().getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_history_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Group chats");

        getYourGroups();
        mRecyclerView = findViewById(R.id.group_chats_list_recycler_view);
        mAdapter = new GroupChatsListAdapter(this, finalGroupsNameList, finalGroupIdList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
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
                //Runtime.getRuntime().exit(0);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getYourGroups() {
        final GetGroupChatsReference groupChatsModel = ViewModelProviders.of(this).get(GetGroupChatsReference.class);
        LiveData<DataSnapshot> groups = groupChatsModel.getAllGroupChats();
        groups.observe(this, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(@Nullable DataSnapshot dataSnapshot) {
                ownerGroupNamesList.clear();
                finalGroupsNameList.clear();
                finalGroupIdList.clear();
                ownergroupIdList.clear();
                for(final DataSnapshot allGroups: dataSnapshot.getChildren()){
                    if(allGroups.child("name").getValue() == null){
                    groupName = "Group";}
                    else{
                    groupName = allGroups.child("name").getValue().toString();}
                    if(allGroups.child("ownerID").getValue() != null) {
                        if (allGroups.child("ownerID").getValue().toString().equals(auth)) {
                            ownergroupIdList.add(allGroups.getKey());
                            ownerGroupNamesList.add(allGroups.child("name").getValue().toString());
                        } else {
                            groupId = allGroups.getKey();
                            LiveData<DataSnapshot> groupMemebers = groupChatsModel.getGroup(groupId);
                            groupMemebers.observe(GroupHistoryList.this, new Observer<DataSnapshot>() {
                                @Override
                                public void onChanged(@Nullable DataSnapshot dataSnapshot) {
                                    for (DataSnapshot members : dataSnapshot.getChildren()) {
                                        if (members.child("memberID").getValue().toString().equals(auth)) {
                                            finalGroupIdList.add(allGroups.getKey());
                                            finalGroupsNameList.add(allGroups.child("name").getValue().toString());
                                        }
                                    }
                                    mAdapter.notifyDataSetChanged();
                                }
                            });
                        }
                    }
                }
                finalGroupsNameList.addAll(ownerGroupNamesList);
                finalGroupIdList.addAll(ownergroupIdList);
                mAdapter.notifyDataSetChanged();
            }
        });

    }
}
