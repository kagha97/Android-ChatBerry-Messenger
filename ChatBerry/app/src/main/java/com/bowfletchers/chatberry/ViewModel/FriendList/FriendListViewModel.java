package com.bowfletchers.chatberry.ViewModel.FriendList;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.bowfletchers.chatberry.ClassLibrary.FirebaseInstances;
import com.bowfletchers.chatberry.DataSource.FirebaseQueryDataOnce;
import com.bowfletchers.chatberry.DataSource.FirebaseQueryLiveData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

public class FriendListViewModel extends ViewModel {

    FirebaseQueryLiveData friendListLiveData;
    FirebaseQueryDataOnce userLiveData;

    public LiveData<DataSnapshot> getFriendListOfCurrentUser(String userId) {
        DatabaseReference friendListRef = FirebaseInstances.getDatabaseReference("/users/" + userId + "/friends");
        friendListLiveData = new FirebaseQueryLiveData(friendListRef);
        return friendListLiveData;
    }

    public LiveData<DataSnapshot> getUserById(String userId) {
        DatabaseReference userRef = FirebaseInstances.getDatabaseReference("/users/" + userId);
        userLiveData = new FirebaseQueryDataOnce(userRef);
        return userLiveData;
    }
}
