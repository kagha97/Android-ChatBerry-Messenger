package com.bowfletchers.chatberry.ClassLibrary;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

public class FirebaseInstances {

    private static final FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
    private static final DatabaseReference mDatabaseRef = mFirebaseDatabase.getReference();
    private static final FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    private static final FirebaseStorage mFirebaseStorage = FirebaseStorage.getInstance();

    public static DatabaseReference getDatabaseReference() {
        return mDatabaseRef;
    }

    public static FirebaseAuth getDatabaseAuth() {
        return mFirebaseAuth;
    }

    public static FirebaseStorage getFirebaseStorage() {
        return mFirebaseStorage;
    }
}
