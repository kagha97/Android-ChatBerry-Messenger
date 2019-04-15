package com.bowfletchers.chatberry.ViewModel.GroupChatsModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.bowfletchers.chatberry.DataSource.FirebaseQueryLiveData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GetGroupChatsReference extends ViewModel {
    private static final DatabaseReference GROUPCHATS = FirebaseDatabase.getInstance().getReference("gchats");
    private final FirebaseQueryLiveData groupChatsLiveData = new FirebaseQueryLiveData(GROUPCHATS);
    public LiveData<DataSnapshot> getGroup(String groupId){
        DatabaseReference groups = FirebaseDatabase.getInstance().getReference("gchats/" + groupId + "/memberList");
        FirebaseQueryLiveData groupsLiveData = new FirebaseQueryLiveData(groups);
        return groupsLiveData;
    }

    public LiveData<DataSnapshot> getAllGroupChats(){return groupChatsLiveData;}
}
