package com.bowfletchers.chatberry.Helper;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.bowfletchers.chatberry.Activities.AvailableUsers;
import com.bowfletchers.chatberry.ClassLibrary.Invitations;
import com.bowfletchers.chatberry.ClassLibrary.Member;
import com.bowfletchers.chatberry.ClassLibrary.Message;
import com.bowfletchers.chatberry.ViewModel.Chat.GetChatRoom;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SendInvitations {
    public boolean invitationAlreadySent = false;

    public Context mcontext;



    private static final DatabaseReference INVITATIONS = FirebaseDatabase.getInstance().getReference("invitations");
    private static final FirebaseAuth auth = FirebaseAuth.getInstance();
    private String userId = auth.getUid();

    public void checkIfAlreadySent(final String receiverId)
    {
        Log.d("Sender" , userId);
        Log.d("Receiver", receiverId);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("invitations");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    Log.d("Receiver" , snapshot.child("receiverId").getValue().toString());
                    String sender = snapshot.child("senderId").getValue().toString();
                    String receiver = snapshot.child("receiverId").getValue().toString();
                    Log.d("Sender" , sender);
                    if((sender.equals(userId) && receiver.equals(receiverId)) || (sender.equals(receiverId) && receiver.equals(userId)))
                    {
                        Log.d("True", receiver);
                        invitationAlreadySent = true;
                    }
                    else {
                        Log.d("Current user" , receiver);
                    }
                }
                if(!invitationAlreadySent)
                {
                    sendInvitation(receiverId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
//        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("invitations");
//        ref.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for(DataSnapshot invitations : dataSnapshot.getChildren())
//                {
//                    String sender = (String) invitations.child("senderID").getValue();
//                    String receiver = (String) invitations.child("receiverID").getValue();
//                    assert sender != null;
//                    assert receiver != null;
//                    if((sender.equals(userId) && receiver.equals(receiverId)))
//                    {
//                        Log.d("True", receiver);
//                        invitationAlreadySent = true;
//                    }
//                    else {
//                        Log.d("Current user" , receiver);
//                    }
//                }
//         //       sendInvitation(receiverId);
//                if(!invitationAlreadySent)
//                {
//                    Log.d("False" , "Not found");
//                    //sendInvitation(receiverId);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//        GetSentInvitations sentInvitations = new GetSentInvitations();
//
//        LiveData<DataSnapshot> liveData = sentInvitations.getSentInvitations();
//        liveData.observe(new Observer<DataSnapshot>() {
//            @Override
//            public void onChanged(@Nullable DataSnapshot dataSnapshot) {
//                for(DataSnapshot snapshot : dataSnapshot.getChildren())
//                {
//                    String userId = auth.getUid();
//                    String sender = snapshot.child("senderID").getValue().toString();
//                    String receiver = snapshot.child("receiverID").getValue().toString();
//                    if(sender.equals(userId) && receiver.equals(receiverId))
//                    {
//                        invitationAlreadySent = true;
//                    }
//                }
//                if(!invitationAlreadySent)
//                {
//                    sendInvitation(receiverId);
//                }
//            }
//        });
    }

    public void sendInvitation(String receiverId)
    {
        Invitations invitations = new Invitations(auth.getUid(), receiverId);
        DatabaseReference newInvitation = INVITATIONS.push();
        newInvitation.setValue(invitations);
    }
}
