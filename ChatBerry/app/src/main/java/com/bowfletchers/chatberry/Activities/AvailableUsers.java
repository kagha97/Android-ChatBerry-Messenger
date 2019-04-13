package com.bowfletchers.chatberry.Activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.bowfletchers.chatberry.Adapters.AvailableUsersInfoAdapter;
import com.bowfletchers.chatberry.ClassLibrary.Member;
import com.bowfletchers.chatberry.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AvailableUsers extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private AvailableUsersInfoAdapter mAdapter;
    private ArrayList<Member> userNames = new ArrayList<>();
    private ArrayList<String> myFriendsIdList = new ArrayList<>();
    private final String auth = FirebaseAuth.getInstance().getUid();
    //private final DatabaseReference myFriends = FirebaseDatabase.getInstance().getReference("/users/" + auth + "/friends" );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_users);
        getMyFriends();
        getAllAvailableUsers();
        mRecyclerView = findViewById(R.id.available_users_recycler_view);
        mAdapter = new AvailableUsersInfoAdapter(userNames, this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //getAllAvailableUsers();
    }

    private void getMyFriends() {
        DatabaseReference users = FirebaseDatabase.getInstance().getReference("users");
        DatabaseReference friends = users.child(auth);
        DatabaseReference friendList = friends.child("friends");
        friendList.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myFriendsIdList.clear();
                for(DataSnapshot myFriends: dataSnapshot.getChildren()){
                    myFriendsIdList.add(myFriends.child("friendId").getValue().toString());
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getAllAvailableUsers()
    {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userNames.clear();
                for(DataSnapshot users : dataSnapshot.getChildren())
                {
                    if(FirebaseAuth.getInstance().getUid().equals(users.child("id").getValue().toString())) {
                        userNames.add(new Member(users.child("id").getValue().toString(),users.child("name").getValue().toString(), users.child("email").getValue().toString(), users.child("profilePicture").getValue().toString(),  false , true));
                    }
                    else if(myFriendsIdList.contains(users.child("id").getValue().toString())){
                        Log.d("Friends", "True");
                        userNames.add(new Member(users.child("id").getValue().toString(),users.child("name").getValue().toString(), users.child("email").getValue().toString(), users.child("profilePicture").getValue().toString(),  false , true));
                    }
                    else {
                        userNames.add(new Member(users.child("id").getValue().toString(), users.child("name").getValue().toString(), users.child("email").getValue().toString(), users.child("profilePicture").getValue().toString()));
                    }
                }
                //Toast.makeText(AvailableUsers.this, userNames.get(1), Toast.LENGTH_LONG).show();
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
