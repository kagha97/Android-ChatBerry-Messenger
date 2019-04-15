package com.bowfletchers.chatberry.ViewModel.Chat;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.bowfletchers.chatberry.ClassLibrary.FirebaseInstances;
import com.bowfletchers.chatberry.DataSource.FirebaseQueryLiveData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LiveChatRoom extends ViewModel {

    private  String CHATID;
    private  DatabaseReference CHAT;

    private FirebaseQueryLiveData liveData;



    @NonNull
    public LiveData<DataSnapshot> getChatRoom(String id) {

        this.CHATID = id;
        CHAT =  FirebaseInstances.getDatabaseReference("/chats/" + CHATID + "/messages");
        liveData = new FirebaseQueryLiveData(CHAT);
        return liveData;
    }



    @NonNull
    public LiveData<DataSnapshot> getGroupChatRoom(String id) {
        this.CHATID = id;
        //CHAT =  FirebaseDatabase.getInstance().getReference("/gchats/" + CHATID + "/messages");
        CHAT =  FirebaseDatabase.getInstance().getReference("/gchats/" + CHATID);
        liveData = new FirebaseQueryLiveData(CHAT);
        return liveData;
    }
}
