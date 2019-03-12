package com.bowfletchers.chatberry;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class ChatHistoryList extends AppCompatActivity {
    private ArrayList<String> friendsName = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private ChatHistoryInfoAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_history_list);
        friendsName.add("Hai");
        friendsName.add("Saad");
        friendsName.add("Khush");
        friendsName.add("Sandy");
        friendsName.add("Brijesh");
        mRecyclerView = findViewById(R.id.chat_history_recycler_view);
        mAdapter = new ChatHistoryInfoAdapter( this, friendsName);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
