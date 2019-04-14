package com.bowfletchers.chatberry.ViewModel.Chat;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.bowfletchers.chatberry.ClassLibrary.FirebaseInstances;
import com.bowfletchers.chatberry.DataSource.FirebaseQueryLiveData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChatViewModel extends ViewModel {
    private static final DatabaseReference CHAT =
            FirebaseInstances.getDatabaseReference("/chats/-Lb68GO9HseFwz7LZc3p/messages");


    private final FirebaseQueryLiveData liveData = new FirebaseQueryLiveData(CHAT);


    @NonNull
    public LiveData<DataSnapshot> getChatRoom() {
        return liveData;
    }




}
