package com.bowfletchers.chatberry.ViewModel.AvailableUsersModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.service.autofill.Dataset;

import com.bowfletchers.chatberry.ClassLibrary.FirebaseInstances;
import com.bowfletchers.chatberry.DataSource.FirebaseQueryLiveData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GetAvailableUsersReference extends ViewModel {
    private static final String authId = FirebaseAuth.getInstance().getUid();
    private static final DatabaseReference USERS =
            FirebaseInstances.getDatabaseReference("users");

    private static final DatabaseReference INVITATIONS =  FirebaseDatabase.getInstance().getReference("invitations");
    private static final DatabaseReference FRIENDS = FirebaseDatabase.getInstance().getReference("users/" + authId + "/friends");
    private final FirebaseQueryLiveData userLiveData = new FirebaseQueryLiveData(USERS);
    private final FirebaseQueryLiveData invitationsLiveData = new FirebaseQueryLiveData(INVITATIONS);
    private final FirebaseQueryLiveData friendsLiveData = new FirebaseQueryLiveData(FRIENDS);


    public LiveData<DataSnapshot> getUsers() {return userLiveData;}

    public LiveData<DataSnapshot> getInvitations() {return  invitationsLiveData;}

    public LiveData<DataSnapshot> getFriends() {return friendsLiveData;}
}
