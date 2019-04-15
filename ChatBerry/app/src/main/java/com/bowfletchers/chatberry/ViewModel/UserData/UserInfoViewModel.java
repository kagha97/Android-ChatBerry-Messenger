package com.bowfletchers.chatberry.ViewModel.UserData;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.bowfletchers.chatberry.ClassLibrary.FirebaseInstances;
import com.bowfletchers.chatberry.DataSource.FirebaseQueryDataOnce;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

public class UserInfoViewModel extends ViewModel {
    FirebaseQueryDataOnce userDataLiveData;

    public LiveData<DataSnapshot> getUserDataById(String userId) {
        DatabaseReference userDataRef =
                FirebaseInstances.getDatabaseReference("/users/" + userId);
        userDataLiveData = new FirebaseQueryDataOnce(userDataRef);
        return userDataLiveData;
    }
}
