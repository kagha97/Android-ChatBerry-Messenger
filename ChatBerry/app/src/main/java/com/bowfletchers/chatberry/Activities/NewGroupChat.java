package com.bowfletchers.chatberry.Activities;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
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
import android.widget.TextView;

import com.bowfletchers.chatberry.Adapters.GCMemberSelectAdapter;
import com.bowfletchers.chatberry.ClassLibrary.FirebaseInstances;
import com.bowfletchers.chatberry.ClassLibrary.GCMember;
import com.bowfletchers.chatberry.ClassLibrary.GroupChat;
import com.bowfletchers.chatberry.ClassLibrary.Member;
import com.bowfletchers.chatberry.R;
import com.bowfletchers.chatberry.ViewModel.GChat.GetMembers;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

public class NewGroupChat extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<Member> mems;
    private GCMemberSelectAdapter memAdapter;
    private TextView groupName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group_chat);
        Toolbar toolbar = findViewById(R.id.toolbar);
        groupName = findViewById(R.id.groupName);
        setSupportActionBar(toolbar);
        setTitle("New Group Chat");
        FloatingActionButton fab = findViewById(R.id.confirm);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();


                Log.i("NMem", "---------");
                for (Member mem : mems) {
                    Log.i("NMem", mem.getName() + ". status:" + String.valueOf(mem.add));
                }

                newGroupChat();


            }
        });



        //set up recycler view
        mems = new ArrayList<>();
       // mems.add(new Member("3422323", "bob", "Online",false, "http://google.ca"));
        //set up recycler view
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        memAdapter = new GCMemberSelectAdapter(this, mems);
        recyclerView.setAdapter(memAdapter);

        loadMembers();
    }



    public void loadMembers() {
        GetMembers members = ViewModelProviders.of(this).get(GetMembers.class);

        LiveData<DataSnapshot> liveData = members.getMembersLive();

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


                    if (!id.equals(FirebaseInstances.getDatabaseAuth().getCurrentUser().getUid())) {
                        Member member = new Member(id, name,"0",false, pfp, "0");
                        mems.add(member);
                    }

                }
                memAdapter.notifyDataSetChanged();
            }
        });
    }

    public void newGroupChat() {
        DatabaseReference chatDb = FirebaseInstances.getDatabaseReference("gchats");
        List<GCMember> checkedMembers = new ArrayList<>();

        for (Member member : mems) {
            if (member.add) {
                GCMember mem = new GCMember(member.getId(), "0");
                checkedMembers.add(mem);
            }
        }

        if (!groupName.getText().toString().equals("") && !groupName.getText().toString().trim().isEmpty()) {
            GCMember mem = new GCMember(FirebaseInstances.getDatabaseAuth().getCurrentUser().getUid(), "1");
            checkedMembers.add(mem);

            GroupChat chat = new GroupChat(groupName.getText().toString(), FirebaseInstances.getDatabaseAuth().getUid(), checkedMembers);

            DatabaseReference newChat = chatDb.push();
            newChat.setValue(chat);

            String chatID = newChat.getKey();

            Intent intent = new Intent(this, GroupMessageViewer.class);

            intent.putExtra("id" , chatID);
            intent.putExtra("owner" , FirebaseInstances.getDatabaseAuth().getUid());
            this.startActivity(intent);

            Log.d("chatstatus", "new chat room created");
            //   Log.d("chatstatus", "chatid: " + chatID);



        }
        else {

            Snackbar.make(recyclerView, "Group chat name can't be empty!",
                    Snackbar.LENGTH_SHORT)
                    .show();
        }



    }

}
