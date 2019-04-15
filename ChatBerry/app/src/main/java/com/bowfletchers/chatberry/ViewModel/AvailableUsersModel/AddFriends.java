package com.bowfletchers.chatberry.ViewModel.AvailableUsersModel;

import com.bowfletchers.chatberry.ClassLibrary.MemberFriendList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddFriends {
    private static final DatabaseReference USERS = FirebaseDatabase.getInstance().getReference("users");
    private static final DatabaseReference INVITATIONS = FirebaseDatabase.getInstance().getReference("invitations");
    private static final FirebaseAuth auth = FirebaseAuth.getInstance();
    private String userId = auth.getUid();

    public void addingFriends(String user, String friend){
        DatabaseReference reference = USERS.child(friend);
        DatabaseReference friends = reference.child("friends");
        MemberFriendList memberFriendList = new MemberFriendList(user);
        DatabaseReference addFriends = friends.push();
        addFriends.setValue(memberFriendList);
        DatabaseReference currentUser = USERS.child(user);
        DatabaseReference currentUserFriends = currentUser.child("friends");
        MemberFriendList currentMemberFriendList = new MemberFriendList(friend);
        DatabaseReference addFriendsToCurrentUser = currentUserFriends.push();
        addFriendsToCurrentUser.setValue(currentMemberFriendList);
    }

    public void removeInvitation(String invitationId){
        DatabaseReference child = INVITATIONS.child(invitationId);
        child.removeValue();
    }
}
