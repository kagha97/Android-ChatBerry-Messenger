package com.bowfletchers.chatberry.ClassLibrary;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

public class FirebaseInstances {

    public static DatabaseReference getDatabaseReference(String root) {
        return FirebaseDatabase.getInstance().getReference(root);
    }

    public static FirebaseAuth getDatabaseAuth() {
        return FirebaseAuth.getInstance();
    }

    public static FirebaseStorage getFirebaseStorage() {
        return FirebaseStorage.getInstance();
    }
}
