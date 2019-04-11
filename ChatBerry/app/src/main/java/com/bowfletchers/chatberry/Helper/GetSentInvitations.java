package com.bowfletchers.chatberry.Helper;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.bowfletchers.chatberry.DataSource.FirebaseQueryDataOnce;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GetSentInvitations extends ViewModel {

    private static final DatabaseReference INVITATIONS =
            FirebaseDatabase.getInstance().getReference("/invitations");

    private final FirebaseQueryDataOnce liveData = new FirebaseQueryDataOnce(INVITATIONS);


    @NonNull
    public LiveData<DataSnapshot> getSentInvitations() {
        return liveData;
    }
}
