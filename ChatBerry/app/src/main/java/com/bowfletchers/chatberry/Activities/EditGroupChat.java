package com.bowfletchers.chatberry.Activities;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bowfletchers.chatberry.Adapters.GCMemberSelectAdapter;
import com.bowfletchers.chatberry.Adapters.GCMemberSettingsAdapter;
import com.bowfletchers.chatberry.ClassLibrary.FirebaseInstances;
import com.bowfletchers.chatberry.ClassLibrary.GCMember;
import com.bowfletchers.chatberry.ClassLibrary.GroupChat;
import com.bowfletchers.chatberry.ClassLibrary.Member;
import com.bowfletchers.chatberry.ClassLibrary.Message;
import com.bowfletchers.chatberry.R;
import com.bowfletchers.chatberry.ViewModel.Chat.LiveChatRoom;
import com.bowfletchers.chatberry.ViewModel.GChat.GetMembers;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static android.support.v7.widget.RecyclerView.*;

public class EditGroupChat extends AppCompatActivity {
    String chatID;
    DatabaseReference mDatabaseReference;
    FirebaseAuth mAuthintication;
    GroupChat chat;
    private RecyclerView recyclerView;
    GCMemberSettingsAdapter memberSettingsAdapter;

    ArrayList<Member> memberList;
    List<Member> oldMemberList;
    TextView title;
    String ownerID;
    Button delete;
    Button addMember;
    Button changeName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_group_chat);
        title = findViewById(R.id.groupName);
        setTitle(getIntent().getStringExtra("title") + "'s Group Settings");

        changeName = findViewById(R.id.changeName);
        memberList = new ArrayList<>();
        oldMemberList = new ArrayList<>();
        chatID = getIntent().getStringExtra("id");
        title.setText(getIntent().getStringExtra("title"));
        getChatRoom(getIntent().getStringExtra("id"));
        mAuthintication = FirebaseInstances.getDatabaseAuth();
        delete = findViewById(R.id.delete_group);
        addMember = findViewById(R.id.add_member);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Log.i("gcsetting", getIntent().getStringExtra("owner"));

        ownerID = getIntent().getStringExtra("owner");
        memberSettingsAdapter = new GCMemberSettingsAdapter(this, memberList, getIntent().getStringExtra("owner"), FirebaseInstances.getDatabaseAuth().getCurrentUser().getUid());
        recyclerView.setAdapter(memberSettingsAdapter);


        memberSettingsAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                Log.i("gcsetting", "changed");
                updateMembers();


              //  memberSettingsAdapter.notifyDataSetChanged();
            }


        });




       if (!FirebaseInstances.getDatabaseAuth().getCurrentUser().getUid().equals(ownerID)) {
           delete.setText(getString(R.string.leave_group));
           addMember.setEnabled(false);
           changeName.setEnabled(false);
       }



    }


    public void updateMembers() {

      //  chat.getMemberList().clear();
        List<GCMember> gmemList = new ArrayList<>();

        for (Member mem : memberList) {
            for (GCMember membr: chat.getMemberList()) {
                if (membr.getMemberID().equals(mem.getId())) {
                    gmemList.add(membr);
                }
            }
       //     GCMember memb = new GCMember(mem.getId(), chat.getMemberList().get())
        }

        chat.setMemberList(gmemList);
        DatabaseReference newChat = FirebaseInstances.getDatabaseReference("gchats/" + chatID);
        newChat.child("memberList").setValue(chat.getMemberList());
    }




    public void getChatRoom(String chatid) {
        LiveChatRoom liveChatRoom = ViewModelProviders.of(this).get(LiveChatRoom.class);
        LiveData<DataSnapshot> liveData = liveChatRoom.getGroupChatRoom(chatid);


        liveData.observe(this, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(@Nullable DataSnapshot dataSnapshot) {
               chat = dataSnapshot.getValue(GroupChat.class);
               List<GCMember> memlist = new ArrayList<>();


                for (DataSnapshot membs : dataSnapshot.child("memberList").getChildren()) {
                    memlist.add(new GCMember(membs.child("memberID").getValue(String.class), membs.child("type").getValue(String.class)));
                }



                chat.setMemberList(memlist);



                Log.i("gcsetting", chat.getName());

                Log.i("gcsetting", String.valueOf(chat.getMemberList().size()));

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
                memberList.clear();
                for (GCMember mem : chat.getMemberList()) {


                        Log.i("gNames", dataSnapshot.child(mem.getMemberID()).child("name").getValue().toString());
                        String id = dataSnapshot.child(mem.getMemberID()).child("id").getValue().toString();
                        String name = dataSnapshot.child(mem.getMemberID()).child("name").getValue().toString();
                        String onlineStatus = dataSnapshot.child(mem.getMemberID()).child("onlineStatus").getValue().toString();
                        //       String status = snapshot.child("onlineStatus").getValue().toString();
                        String pfp = dataSnapshot.child(mem.getMemberID()).child("profilePicture").getValue().toString();
                        Member member = new Member(id, name, onlineStatus, false, pfp,mem.getType());
                        memberList.add(member);


                }
                oldMemberList = memberList;
                memberSettingsAdapter.notifyDataSetChanged();

            }

        });
    }


    public void changeName(View view) {

        if (mAuthintication.getCurrentUser().getUid().equals(chat.getOwnerID())) {
            chat.setName(title.getText().toString());
            DatabaseReference newChat = FirebaseInstances.getDatabaseReference("gchats/" + chatID);
            newChat.child("name").setValue(title.getText().toString());
            Snackbar.make(recyclerView, "Group name updated to: " + title.getText().toString(),
                    Snackbar.LENGTH_SHORT)
                    .show();
        }
        else {
            Snackbar.make(recyclerView, "Only the group owner can update name.",
                    Snackbar.LENGTH_SHORT)
                    .show();
        }

    }

    public void addMember(View view) {
        if (mAuthintication.getCurrentUser().getUid().equals(chat.getOwnerID())) {
            Intent editGCIntent = new Intent(EditGroupChat.this, AddMemberToGroupChat.class);
            editGCIntent.putExtra("id", mAuthintication.getCurrentUser().getUid());
            editGCIntent.putExtra("chatid", chatID);
            startActivity(editGCIntent);
        }
        else {
            Snackbar.make(recyclerView, "Only the group owner can add members.",
                    Snackbar.LENGTH_SHORT)
                    .show();
        }
    }

    public void deleteGroup(View view) {

        if (!FirebaseInstances.getDatabaseAuth().getCurrentUser().getUid().equals(ownerID)) {


            List<GCMember> newMem = new ArrayList<>();
            for (GCMember mem : chat.getMemberList()) {
                if (!mem.getMemberID().equals(FirebaseInstances.getDatabaseAuth().getCurrentUser().getUid())) {
                   newMem.add(mem);
                }
            }

            DatabaseReference newChat = FirebaseInstances.getDatabaseReference("gchats/" + chatID);
            newChat.child("memberList").setValue(newMem);
            Intent intent = new Intent(this, ChatHistoryList.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            this.startActivity(intent);
          //  Runtime.getRuntime().exit(0);
        }
        else {
            DatabaseReference chatDb = FirebaseInstances.getDatabaseReference("gchats").child(chatID);
            chatDb.removeValue();
          /* Intent intent = new Intent(this, ChatHistoryList.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();*/


            Intent intent = new Intent(this, ChatHistoryList.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            this.startActivity(intent);
            Runtime.getRuntime().exit(0);
        }

    }
}