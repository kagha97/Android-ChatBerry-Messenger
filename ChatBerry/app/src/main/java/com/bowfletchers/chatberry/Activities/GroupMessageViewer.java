package com.bowfletchers.chatberry.Activities;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bowfletchers.chatberry.Adapters.MessagesAdapter;
import com.bowfletchers.chatberry.ClassLibrary.FirebaseInstances;
import com.bowfletchers.chatberry.ClassLibrary.Member;
import com.bowfletchers.chatberry.ClassLibrary.Message;
import com.bowfletchers.chatberry.R;
import com.bowfletchers.chatberry.ViewModel.Chat.LiveChatRoom;
import com.bowfletchers.chatberry.ViewModel.Chat.SendMessage;
import com.bowfletchers.chatberry.ViewModel.GChat.GetMembers;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class GroupMessageViewer extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MessagesAdapter msgAdapter;
    TextView newMessage;
    TextView tempView;
    ArrayList<Message> msgs;
    ArrayList<Member> memberList;
    ArrayList<String> memberIDList;
    ArrayList<Member> members;
    FirebaseAuth mAuthentication;
    DatabaseReference mDatabase;
    Context context;
    String chatID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_message_viewer);


        newMessage = findViewById(R.id.messageSend);

        mAuthentication = FirebaseInstances.getDatabaseAuth();
        mDatabase = FirebaseInstances.getDatabaseReference("gchats");
        context = this;
        msgs = new ArrayList<>();
        memberIDList = new ArrayList<>();
        memberList = new ArrayList<>();
        //set up recycler view
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        msgAdapter = new MessagesAdapter(this, msgs);
        recyclerView.setAdapter(msgAdapter);

        Log.i("gchat", "chat id: " +getIntent().getStringExtra("id"));
        chatID = getIntent().getStringExtra("id");
        liveChatRoom(chatID, context);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.gc_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Intent editGCIntent = new Intent(GroupMessageViewer.this, EditGroupChat.class);
                editGCIntent.putExtra("id", chatID);
                editGCIntent.putExtra("title", getTitle().toString());
                editGCIntent.putExtra("owner", getIntent().getStringExtra("owner"));
                startActivity(editGCIntent);
                return true;
            case R.id.leave:
             //   Intent userFriendsIntent = new Intent(GroupMessageViewer.this, FriendList.class);
               // startActivity(userFriendsIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }





    public void liveChatRoom(String chatid, Context ctx) {
        LiveChatRoom liveChatRoom = ViewModelProviders.of(this).get(LiveChatRoom.class);
        LiveData<DataSnapshot> liveData = liveChatRoom.getGroupChatRoom(chatid);


        liveData.observe(this, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(@Nullable DataSnapshot dataSnapshot) {
                msgs.clear();
                memberIDList.clear();

                setTitle(dataSnapshot.child("name").getValue(String.class));

                for (DataSnapshot msgSnapshot: dataSnapshot.child("memberList").getChildren()) {
                    Log.i("gchat", "ID: " + msgSnapshot.child("memberID").getValue().toString());
                    memberIDList.add(msgSnapshot.child("memberID").getValue().toString());
                }

                memberIDList.add(dataSnapshot.child("ownerID").getValue(String.class));

                getMemberList();

                 //   Log.i("gmessage", "Message: " + msgSnapshot.child("content").getValue(String.class) + " Sender: " + msgSnapshot.child("senderID").getValue(String.class));
                    //  Log.i(LOG_TAG, "NAME function: " + getSenderName(msgSnapshot.child("senderID").getValue(String.class)));

                    msgs.clear();
                    for (DataSnapshot rmsgs : dataSnapshot.child("messages").getChildren()) {
                        Member member = new Member();
                        Log.i("member","-----  ");
                        for (Member mem : memberList) {
                            Log.i("member", mem.name);
                                if (mem.getId().equals(rmsgs.child("senderID").getValue().toString())) {
                                    member = mem;
                                }

                        }

                        Message message = new Message(member.getName(), rmsgs.child("message").getValue(String.class), member.getProfilePicture());
                        msgs.add(message);
                    }

                 //  Message message = new Message(name, msgSnapshot.child("content").getValue(String.class),pfp);
               //     Log.i(LOG_TAG, "MSG OBJ: " + message.getMessage());

                    //Log.i("IDofsender", "ID of object: " + message.senderID);


                Log.i("gchat", "Group name: " + dataSnapshot.child("name").getValue(String.class));

                msgAdapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(msgs.size() - 1);

            }
        });
    }


    public void getMemberList () {
        GetMembers liveChatRoom = ViewModelProviders.of(this).get(GetMembers.class);
        LiveData<DataSnapshot> memberData = liveChatRoom.getMembersOnce();


        memberData.observe(this, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(@Nullable DataSnapshot dataSnapshot) {
                memberList.clear();
                for (String id : memberIDList) {
                    String name = dataSnapshot.child(id).child("name").getValue().toString();
                    String uid = dataSnapshot.child(id).child("id").getValue().toString();
                    String pfp = dataSnapshot.child(id).child("profilePicture").getValue().toString();
                    String onlineStatus;
                    if (dataSnapshot.child(id).child("onlineStatus").getValue() != null) {
                        onlineStatus = "1";
                    }
                    else {
                        onlineStatus = "0";
                    }


                    Member member = new Member(uid, name, onlineStatus, false, pfp, "0");
                    memberList.add(member);
                }

                Log.i("member", String.valueOf(memberList.size()));
            }
        });

    }

    public void sendMessage(View view) {
//        Log.d("chatid", chatID);

        SendMessage sendMessage = new SendMessage();


        sendMessage.sendGroupMessage(chatID, newMessage.getText().toString(), mAuthentication.getCurrentUser().getPhotoUrl().toString());
        newMessage.setText("");

        recyclerView.scrollToPosition(msgs.size() - 1);
    }

    public void scrollToBottom(View view) {
        recyclerView.scrollToPosition(msgs.size() - 1);
    }




}
