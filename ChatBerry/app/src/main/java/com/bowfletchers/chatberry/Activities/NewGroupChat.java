package com.bowfletchers.chatberry.Activities;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.bowfletchers.chatberry.Adapters.GCMemberSelectAdapter;
import com.bowfletchers.chatberry.Adapters.MessagesAdapter;
import com.bowfletchers.chatberry.ClassLibrary.GCMember;
import com.bowfletchers.chatberry.R;
import com.bowfletchers.chatberry.ViewModel.Chat.GetChatRoom;
import com.bowfletchers.chatberry.ViewModel.GChat.GetMembers;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;

public class NewGroupChat extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<GCMember> mems;
    private GCMemberSelectAdapter memAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group_chat);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("New Group Chat");
        FloatingActionButton fab = findViewById(R.id.confirm);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();


                Log.i("NMem", "---------");
                for (GCMember mem : mems) {
                    Log.i("NMem", mem.getName() + ". status:" + String.valueOf(mem.getAdd()));
                }


            }
        });



        //set up recycler view
        mems = new ArrayList<>();
        mems.add(new GCMember("3422323", "bob", "Online",false, "http://google.ca"));
        //set up recycler view
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        memAdapter = new GCMemberSelectAdapter(this, mems);
        recyclerView.setAdapter(memAdapter);

        loadMembers();
    }



    public void loadMembers() {
        GetMembers members = ViewModelProviders.of(this).get(GetMembers.class);

        LiveData<DataSnapshot> liveData = members.getMembers();

        liveData.observe(this, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(@Nullable DataSnapshot dataSnapshot) {
                mems.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.i("gNames", snapshot.child("name").getValue().toString());
                    String id = snapshot.child("id").getValue().toString();
                    String name = snapshot.child("name").getValue().toString();
             //       String status = snapshot.child("onlineStatus").getValue().toString();
                    String pfp = snapshot.child("profilePicture").getValue().toString();


                    GCMember member = new GCMember(id, name,"0",false, pfp);
                    mems.add(member);
                }
                memAdapter.notifyDataSetChanged();
            }
        });
    }

}
