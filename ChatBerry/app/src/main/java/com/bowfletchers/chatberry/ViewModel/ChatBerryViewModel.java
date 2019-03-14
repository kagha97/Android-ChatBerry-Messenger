package com.bowfletchers.chatberry.ViewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.bowfletchers.chatberry.Repository.ChatBerryLiveData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChatBerryViewModel extends ViewModel {
    private static final DatabaseReference CHAT_BERRY_FIREBASE_REF =
            FirebaseDatabase.getInstance().getReference();

    private final ChatBerryLiveData liveData = new ChatBerryLiveData(CHAT_BERRY_FIREBASE_REF);

    @NonNull
    public LiveData<DataSnapshot> getDataSnapshotLiveData() {
        return liveData;
    }
}
