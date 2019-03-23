package com.bowfletchers.chatberry.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.bowfletchers.chatberry.Adapters.ChatHistoryInfoAdapter;
import com.bowfletchers.chatberry.ClassLibrary.Member;
import com.bowfletchers.chatberry.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChatHistoryList extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private ChatHistoryInfoAdapter mAdapter;

    private Member logInMember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_history_list);

        mRecyclerView = findViewById(R.id.chat_history_recycler_view);
        mAdapter = new ChatHistoryInfoAdapter( this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        logInMember = (Member) getIntent().getSerializableExtra("NewUser");

        setTitle("Welcome " + logInMember.getName());
        Toast.makeText(this, logInMember.getEmail(), Toast.LENGTH_SHORT).show();
    }

}
