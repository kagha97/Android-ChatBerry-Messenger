package com.bowfletchers.chatberry.Activities;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bowfletchers.chatberry.Adapters.MessagesAdapter;
import com.bowfletchers.chatberry.ClassLibrary.Chat;
import com.bowfletchers.chatberry.ClassLibrary.FirebaseInstances;
import com.bowfletchers.chatberry.ClassLibrary.Member;
import com.bowfletchers.chatberry.ClassLibrary.Message;
import com.bowfletchers.chatberry.R;
import com.bowfletchers.chatberry.ViewModel.Chat.GetChatRoom;
import com.bowfletchers.chatberry.ViewModel.Chat.LiveChatRoom;
import com.bowfletchers.chatberry.ViewModel.Chat.SendMessage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MessageViewer extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    FirebaseAuth mAuthentication;
    DatabaseReference mDatabase;
    TextView newMessage;
    TextView tempView;
    ArrayList<Message> msgs;
    private RecyclerView recyclerView;
    private MessagesAdapter msgAdapter;
    private static final String LOG_TAG = MessageViewer.class.getSimpleName();
    private String NAME;
    Member member;
    private String chatID;
    private String memName;
    private Boolean isChatNew = false;
    private List<String> chatNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_viewer);
        newMessage = findViewById(R.id.messageSend);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuthentication = FirebaseInstances.getDatabaseAuth();
        mDatabase = FirebaseDatabase.getInstance().getReference("chats");
        tempView = new TextView(this);


        msgs = new ArrayList<>();
        msgs.add(new Message(mAuthentication.getUid(),"test", "google.ca"));
        //set up recycler view
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        msgAdapter = new MessagesAdapter(this, msgs);
        recyclerView.setAdapter(msgAdapter);


        member = (Member) getIntent().getSerializableExtra("chatMember");
        getChatRoom();

        Spinner spinner = (Spinner) findViewById(R.id.emoji);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.emojis, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        setTitle(member.name);
    }



    public void getChatRoom() {
        GetChatRoom chatViewModel = ViewModelProviders.of(this).get(GetChatRoom.class);

        LiveData<DataSnapshot> liveData = chatViewModel.getChatRoom();

        liveData.observe(this, new Observer<DataSnapshot>() {

            @Override
            public void onChanged(@Nullable DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String currentID = mAuthentication.getCurrentUser().getUid();
                    String memberID = member.id;

                    String sender = snapshot.child("senderID").getValue(String.class);
                    String receiver = snapshot.child("receiverID").getValue(String.class);

                    Log.d("chatstatus", "looking for " + memberID);
                    Log.d("chatstatus", "I am " + currentID);

                    if (sender.equals(currentID) && receiver.equals(memberID) ||
                            sender.equals(memberID) && receiver.equals(currentID)) {

                        Log.d("chatstatus", "found");
                        chatID = snapshot.getKey();
                        isChatNew = false;
                        Log.d("chatstatus", snapshot.getKey());
                        break;

                    }
                    else if (!sender.equals(currentID) && !receiver.equals(memberID) ||
                            !sender.equals(memberID) && !receiver.equals(currentID)) {

                        isChatNew = true;
                        //  newChatRoom();
                        Log.d("chatstatus", "not found");
                        Log.d("chatstatus", "n " + snapshot.getKey());
                        Log.d("chatstatus", "n sender " + sender);
                        Log.d("chatstatus", "n rec " + receiver);

                    }
                }

                if (isChatNew) {
                    newChatRoom();
                }

                else {
                    liveChatRoom(chatID);
                }
            }


            });
    }

    public void liveChatRoom(String chatid) {
        LiveChatRoom liveChatRoom = ViewModelProviders.of(this).get(LiveChatRoom.class);
        LiveData<DataSnapshot> liveData = liveChatRoom.getChatRoom(chatid);


        liveData.observe(this, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(@Nullable DataSnapshot dataSnapshot) {
                msgs.clear();
                for (DataSnapshot msgSnapshot: dataSnapshot.getChildren()) {

                    Log.i(LOG_TAG, "Message: " + msgSnapshot.child("message").getValue(String.class) + " Sender: " + msgSnapshot.child("senderID").getValue(String.class));
                    //  Log.i(LOG_TAG, "NAME function: " + getSenderName(msgSnapshot.child("senderID").getValue(String.class)));
                    String name = "";
                    String pfp = "";

                    if (msgSnapshot.child("senderID").getValue(String.class).equals(mAuthentication.getUid())) {
                        name = mAuthentication.getCurrentUser().getDisplayName();
                        pfp = mAuthentication.getCurrentUser().getPhotoUrl().toString();
                    }
                    else if (msgSnapshot.child("senderID").getValue(String.class).equals(member.getId())) {
                        name = member.getName();
                        pfp = member.getProfilePicture();
                    }

                    Log.d("thename", name);

                    Log.i("IDofsender", "ID: " + msgSnapshot.child("senderID"));
                    Message message = new Message(name, msgSnapshot.child("message").getValue(String.class),pfp);
                    Log.i(LOG_TAG, "MSG OBJ: " + message.getMessage());

                    Log.i("IDofsender", "ID of object: " + message.senderID);
                    msgs.add(message);
                }
                msgAdapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(msgs.size() - 1);

            }
        });
    }



    public void newChatRoom() {
        Log.i("newchat", "creating new chat room");
        DatabaseReference chatDb = mDatabase;

        Chat chat = new Chat(member.id, mAuthentication.getUid());

        DatabaseReference newChat = chatDb.push();
        newChat.setValue(chat);

        chatID = newChat.getKey();

        liveChatRoom(chatID);


        Log.d("chatstatus", "new chat room created");
        Log.d("chatstatus", "chatid: " + chatID);

    }



    public void displayToast(String message){
        Toast.makeText(this, message,Toast.LENGTH_SHORT).show();
    }


    //create display name in message
    public void sendMessage(View view) {

//        Log.d("chatid", chatID);

        if (!newMessage.getText().toString().equals("") && !newMessage.getText().toString().trim().isEmpty()){
            SendMessage sendMessage = new SendMessage();


            sendMessage.sendMessage(chatID, newMessage.getText().toString(), mAuthentication.getCurrentUser().getPhotoUrl().toString());
            newMessage.setText("");
            recyclerView.scrollToPosition(msgs.size() - 1);

        }

        else {
            Snackbar.make(recyclerView, "Can't send an empty message!",
                    Snackbar.LENGTH_SHORT)
                    .show();
        }

    }

    public void scrollToBottom(View view) {
        recyclerView.scrollToPosition(msgs.size() - 1);
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

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        newMessage.setText(newMessage.getText() + parent.getItemAtPosition(position).toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}