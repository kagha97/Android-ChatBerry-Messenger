package com.bowfletchers.chatberry.Activities;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.bowfletchers.chatberry.Adapters.GCMemberSelectAdapter;
import com.bowfletchers.chatberry.ClassLibrary.FirebaseInstances;
import com.bowfletchers.chatberry.ClassLibrary.GCMember;
import com.bowfletchers.chatberry.ClassLibrary.GroupChat;
import com.bowfletchers.chatberry.ClassLibrary.Member;
import com.bowfletchers.chatberry.ClassLibrary.Message;
import com.bowfletchers.chatberry.R;
import com.bowfletchers.chatberry.ViewModel.Chat.LiveChatRoom;
import com.bowfletchers.chatberry.ViewModel.GChat.GetMembers;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

public class AddMemberToGroupChat extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<Member> tmems;
    private ArrayList<Member> omems;
    String userID;
    String chatID;
    ArrayList<String> idList;
    private GCMemberSelectAdapter memAdapter;
    private GroupChat chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member_to_group_chat);

        setTitle("Add members");
        userID = getIntent().getStringExtra("id");
        chatID = getIntent().getStringExtra("chatid");

        idList = new ArrayList<>();
        tmems = new ArrayList<>();
        omems = new ArrayList<>();
        // mems.add(new Member("3422323", "bob", "Online",false, "http://google.ca"));
        //set up recycler view
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        memAdapter = new GCMemberSelectAdapter(this, tmems);
        recyclerView.setAdapter(memAdapter);

        getExistingGroupMembers();

    }



    public void getExistingGroupMembers() {
        LiveChatRoom liveChatRoom = ViewModelProviders.of(this).get(LiveChatRoom.class);
        LiveData<DataSnapshot> liveData = liveChatRoom.getGroupChatRoom(chatID);


        liveData.observe(this, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(@Nullable DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.child("memberList").getChildren()) {

                    idList.add(snapshot.child("memberID").getValue(String.class));
                }

                loadMembers();


            }
        });
    }



    public void loadMembers() {
        final GetMembers members = ViewModelProviders.of(this).get(GetMembers.class);

        LiveData<DataSnapshot> liveData = members.getMembersOnce();

        liveData.observe(this, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(@Nullable DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {


                        if (!snapshot.child("id").equals(userID)) {
                            Log.i("gNames", snapshot.child("name").getValue().toString());
                            String id = snapshot.child("id").getValue().toString();
                            String name = snapshot.child("name").getValue().toString();
                            //       String status = snapshot.child("onlineStatus").getValue().toString();
                            String pfp = snapshot.child("profilePicture").getValue().toString();
                            String onlineStatus = snapshot.child("onlineStatus").getValue().toString();

                            Member member = new Member(id, name,onlineStatus,false, pfp,"0");
                            tmems.add(member);
                        }



                    for (String mid : idList) {
                        String id = snapshot.child("id").getValue().toString();
                        if (id.equals(mid)) {

                            String name = snapshot.child("name").getValue().toString();
                            //       String status = snapshot.child("onlineStatus").getValue().toString();
                            String pfp = snapshot.child("profilePicture").getValue().toString();

                            Member member = new Member(id, name,"0",false, pfp, "0");

                            omems.add(member);
                        }
                    }


                    ArrayList<Member> toRemove = new ArrayList<>();
                    for (Member mem : tmems) {
                        for (String mid : idList) {
                            if (mem.getId().equals(mid)) {
                                toRemove.add(mem);
                            }
                        }
                    }

                    for (Member mem : toRemove) {
                        tmems.remove(mem);
                    }





                    }


                memAdapter.notifyDataSetChanged();
            }


        });


    }

    public void addMember(View view) {

        List<GCMember> checkedMembers = new ArrayList<>();

        for (Member member : tmems) {
            if (member.add) {
                GCMember mem = new GCMember(member.getId(), "0");
                checkedMembers.add(mem);
            }
        }

        for (Member member : omems) {
            if (member.getId().equals(userID)){
                GCMember mem = new GCMember(member.getId(), "1");
                checkedMembers.add(mem);
            }
            else {
                GCMember mem = new GCMember(member.getId(), "0");
                checkedMembers.add(mem);
            }
            }

        Log.i("amembers", "------------");
            for (GCMember mem : checkedMembers) {
                Log.i("amembers", mem.getMemberID());
            }


       DatabaseReference newChat = FirebaseInstances.getDatabaseReference("gchats/" + chatID);
        newChat.child("memberList").setValue(checkedMembers);

        Snackbar.make(view, "Members Added.",
                Snackbar.LENGTH_SHORT)
                .show();

       finish();

        }



    }

