package com.bowfletchers.chatberry.ViewModel.Chat;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.bowfletchers.chatberry.ClassLibrary.FirebaseInstances;
import com.bowfletchers.chatberry.DataSource.FirebaseQueryDataOnce;
import com.bowfletchers.chatberry.DataSource.FirebaseQueryLiveData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GetChatRoom extends ViewModel {


    private static final DatabaseReference CHATS =
            FirebaseInstances.getDatabaseReference("/chats");

    private final FirebaseQueryDataOnce liveData = new FirebaseQueryDataOnce(CHATS);


    @NonNull
    public LiveData<DataSnapshot> getChatRoom() {
        return liveData;
    }

}
