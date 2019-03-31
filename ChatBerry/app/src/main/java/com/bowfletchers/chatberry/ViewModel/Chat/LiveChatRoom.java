package com.bowfletchers.chatberry.ViewModel.Chat;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.bowfletchers.chatberry.DataSource.FirebaseQueryLiveData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LiveChatRoom extends ViewModel {

    private final String CHATID;
    private final DatabaseReference CHAT;

    private final FirebaseQueryLiveData liveData;

    public LiveChatRoom (String id) {
        this.CHATID = id;
        CHAT =  FirebaseDatabase.getInstance().getReference("/chats/" + CHATID + "/messages");
        liveData = new FirebaseQueryLiveData(CHAT);
    }

    @NonNull
    public LiveData<DataSnapshot> getChatRoom() {
        return liveData;
    }
}
