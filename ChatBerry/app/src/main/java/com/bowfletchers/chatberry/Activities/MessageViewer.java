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
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bowfletchers.chatberry.Adapters.MessagesAdapter;
import com.bowfletchers.chatberry.ClassLibrary.Chat;
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

public class MessageViewer extends AppCompatActivity {
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
        //getChatRoom();

        mAuthentication = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("chats");
        tempView = new TextView(this);


        msgs = new ArrayList<>();
        //set up recycler view
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        msgAdapter = new MessagesAdapter(this, msgs);
        recyclerView.setAdapter(msgAdapter);


        member = (Member) getIntent().getSerializableExtra("chatMember");
        getChatRoom();


        setTitle(member.name);
    }



    public void getChatRoom() {
        GetChatRoom chatViewModel = ViewModelProviders.of(this).get(GetChatRoom.class);

        LiveData<DataSnapshot> liveData = chatViewModel.getChatRoom();

        liveData.observe(this, new Observer<DataSnapshot>() {

            @Override
            public void onChanged(@Nullable DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String currentID = mAuthentication.getUid();
                    String memberID = member.id;

                    String sender = snapshot.child("senderID").getValue().toString();
                    String receiver = snapshot.child("receiverID").getValue().toString();

                    Log.d("chatstatus", "looking for " + memberID);
                    Log.d("chatstatus", "I am " + currentID);

                    if (sender.equals(mAuthentication.getUid()) && receiver.equals(member.id) ||
                            sender.equals(member.id) && receiver.equals(mAuthentication.getUid())) {

                        Log.d("chatstatus", "found");
                        chatID = snapshot.getKey();
                        isChatNew = false;
                        Log.d("chatstatus", snapshot.getKey());
                        break;

                    }
                    else if (!sender.equals(mAuthentication.getUid()) && !receiver.equals(member.id) ||
                            !sender.equals(member.id) && !receiver.equals(mAuthentication.getUid())) {

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

                    Log.i(LOG_TAG, "Message: " + msgSnapshot.child("content").getValue(String.class) + " Sender: " + msgSnapshot.child("senderID").getValue(String.class));
                    //  Log.i(LOG_TAG, "NAME function: " + getSenderName(msgSnapshot.child("senderID").getValue(String.class)));
                    String name = "";

                    if (msgSnapshot.child("senderID").getValue(String.class).equals(mAuthentication.getUid()))
                        name = mAuthentication.getCurrentUser().getDisplayName();
                    else if (msgSnapshot.child("senderID").getValue(String.class).equals(member.getId()))
                        name = member.getName();

                    Log.d("thename", name);

                    Log.i("IDofsender", "ID: " + msgSnapshot.child("senderID"));
                    Message message = new Message(name, msgSnapshot.child("content").getValue(String.class));
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

        SendMessage sendMessage = new SendMessage();


        sendMessage.sendMessage(chatID, newMessage.getText().toString());
        newMessage.setText("");
        recyclerView.scrollToPosition(msgs.size() - 1);

    }

    public void scrollToBottom(View view) {
        recyclerView.scrollToPosition(msgs.size() - 1);
    }
}