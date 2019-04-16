package com.bowfletchers.chatberry.Activities;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

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
        getYourGroups();
        mRecyclerView = findViewById(R.id.group_chats_list_recycler_view);
        mAdapter = new GroupChatsListAdapter(this, finalGroupsNameList, finalGroupIdList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
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
