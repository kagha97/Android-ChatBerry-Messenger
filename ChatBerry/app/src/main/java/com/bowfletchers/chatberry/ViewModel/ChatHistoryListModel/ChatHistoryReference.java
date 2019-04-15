package com.bowfletchers.chatberry.ViewModel.ChatHistoryListModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.bowfletchers.chatberry.ClassLibrary.FirebaseInstances;
import com.bowfletchers.chatberry.DataSource.FirebaseQueryDataOnce;
import com.bowfletchers.chatberry.DataSource.FirebaseQueryLiveData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

public class ChatHistoryReference extends ViewModel {
    private static final DatabaseReference USERS = FirebaseInstances.getDatabaseReference("users");
    private static final DatabaseReference CHATS = FirebaseInstances.getDatabaseReference("chats");

    private final FirebaseQueryDataOnce userLiveData = new FirebaseQueryDataOnce(USERS);
    private final FirebaseQueryLiveData chatsLiveData = new FirebaseQueryLiveData(CHATS);

    public LiveData<DataSnapshot> getUsers() {return userLiveData;}

    public LiveData<DataSnapshot> getChats() {return chatsLiveData;}
}
