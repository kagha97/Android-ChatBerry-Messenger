package com.bowfletchers.chatberry.Activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.bowfletchers.chatberry.Adapters.AvailableUsersInfoAdapter;
import com.bowfletchers.chatberry.Adapters.FriendRequestAdapter;
import com.bowfletchers.chatberry.ClassLibrary.Member;
import com.bowfletchers.chatberry.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FriendRequests extends AppCompatActivity {
    private String userId = FirebaseAuth.getInstance().getUid();
    public List<Member> requesteeList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private FriendRequestAdapter mAdapter;
    private String invitationId;
    private List<String> invitationList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_requests);
        getAllFriendRequests();
        mRecyclerView = findViewById(R.id.requestee_recycler_view);
        mAdapter = new FriendRequestAdapter(this, requesteeList, invitationList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void getAllFriendRequests() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("invitations");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    final String receiver = snapshot.child("receiverId").getValue().toString();
                    final String sender = snapshot.child("senderId").getValue().toString();
                    if(receiver.equals(userId)) {
                        invitationId = snapshot.getKey();
                        Log.d("Request", "You have a request");
                        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("users");
                        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for(DataSnapshot userSnapshot : dataSnapshot.getChildren())
                                {
                                    if(userSnapshot.child("id").getValue().toString().equals(sender)) {
                                        Log.d("Name", userSnapshot.child("name").getValue().toString());
                                        invitationList.add(invitationId);
                                        requesteeList.add(new Member(userSnapshot.child("id").getValue().toString(), userSnapshot.child("name").getValue().toString(), userSnapshot.child("email").getValue().toString(), userSnapshot.child("profilePicture").getValue().toString()));
                                    }
                                }
                                mAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
