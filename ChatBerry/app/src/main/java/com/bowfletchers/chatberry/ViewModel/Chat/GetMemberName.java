package com.bowfletchers.chatberry.ViewModel.Chat;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.util.Log;

import com.bowfletchers.chatberry.DataSource.FirebaseQueryDataOnce;
import com.bowfletchers.chatberry.DataSource.FirebaseQueryLiveData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GetMemberName extends ViewModel {
    private  DatabaseReference USER;
    private FirebaseQueryDataOnce data;

    @NonNull
    public LiveData<DataSnapshot> getMemberName(String id) {
        USER =  FirebaseDatabase.getInstance().getReference("users/" + id);
        data = new FirebaseQueryDataOnce(USER);
        return data;
    }
}
