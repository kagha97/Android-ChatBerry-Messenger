package com.bowfletchers.chatberry.ViewModel.GChat;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.bowfletchers.chatberry.DataSource.FirebaseQueryLiveData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GetMembers extends ViewModel {
    private DatabaseReference CHAT;

    private FirebaseQueryLiveData liveData;



    @NonNull
    public LiveData<DataSnapshot> getMembers() {
        CHAT =  FirebaseDatabase.getInstance().getReference("/users");
        liveData = new FirebaseQueryLiveData(CHAT);
        return liveData;
    }
}
