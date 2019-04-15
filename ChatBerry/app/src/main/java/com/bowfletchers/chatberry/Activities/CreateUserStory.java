package com.bowfletchers.chatberry.Activities;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SimpleCursorTreeAdapter;
import android.widget.Toast;

import com.bowfletchers.chatberry.ClassLibrary.FirebaseInstances;
import com.bowfletchers.chatberry.ClassLibrary.UserStory;
import com.bowfletchers.chatberry.R;
import com.bowfletchers.chatberry.ViewModel.UserStory.UserStoryViewModel;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class CreateUserStory extends AppCompatActivity {

    private final int REQUEST_CODE_IMAGE = 1;
    private final String STORE_URL = "gs://chatberry-201de.appspot.com";


    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference userDataReference;
    private FirebaseStorage mDataStore;
    private StorageReference databaseStoreRef;

    private ImageView imageViewStoryPhoto;
    private ImageButton imageButtonCapturePhoto;
    private EditText editTextStatus;
    private Button buttonUpdate;
    private Button buttonCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user_story);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        referenceViews();
        referenceFirebaseInstances();

        // display current user story when page load
        String userId = currentUser.getUid();
        //displayUserStory(userId);

        // handle cancel button
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goBackHomeIntent = new Intent(CreateUserStory.this, ChatHistoryList.class);
                startActivity(goBackHomeIntent);
            }
        });

        // handle Update button click
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserStory();
            }
        });

        // handle capture camera button click
        imageButtonCapturePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, REQUEST_CODE_IMAGE);            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // get the captured image to image view
        if (requestCode == REQUEST_CODE_IMAGE && resultCode == RESULT_OK && data != null) {
            Bitmap bitmapImg = (Bitmap) data.getExtras().get("data");
            imageViewStoryPhoto.setImageBitmap(bitmapImg);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.my_profile:
                Intent userProfileIntent = new Intent(CreateUserStory.this, UserProfile.class);
                startActivity(userProfileIntent);
                return true;
            case R.id.my_friends:
                Intent userFriendsIntent = new Intent(CreateUserStory.this, FriendList.class);
                startActivity(userFriendsIntent);
                return true;
            case R.id.homePage:
                Intent chatListIntent = new Intent(CreateUserStory.this, ChatHistoryList.class);
                startActivity(chatListIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void displayUserStory(String userId) {
        // read database for user's story and populate it to view
        UserStoryViewModel userStoryViewModel = ViewModelProviders.of(this).get(UserStoryViewModel.class);

        LiveData<DataSnapshot> userStoryLiveData = userStoryViewModel.getUserStoryById(userId);

        userStoryLiveData.observe(this, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(@Nullable DataSnapshot dataSnapshot) {

                UserStory userStory = dataSnapshot.getValue(UserStory.class);

                // map data to views
                Glide.with(CreateUserStory.this).load(userStory.getPhotoStoryURL()).into(imageViewStoryPhoto);
                editTextStatus.setText(userStory.getStatusMessage());
            }
        });
    }

    public void updateUserStory() {
        // set components to create img name
        String userName = currentUser.getDisplayName();
        String userId = currentUser.getUid();
        String imageName = userName + userId + "story.png";

        // upload current img to store
        final StorageReference userStoryImageRef = databaseStoreRef.child("userStories/" + imageName);
        imageViewStoryPhoto.setDrawingCacheEnabled(true);
        imageViewStoryPhoto.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) imageViewStoryPhoto.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = userStoryImageRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(CreateUserStory.this, "Upload story image failed", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // get user img download url from store
                userStoryImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String downloadUrl = uri.toString();
                        // update user data in database with their img and status message
                        String userStatusMessage;
                        String statusInput = editTextStatus.getText().toString();
                        if (statusInput.equals("")) {
                            userStatusMessage = "I dont have any status now";
                        } else {
                            userStatusMessage = statusInput;
                        }
                        updateUserStoryInDatabase(downloadUrl, userStatusMessage);
                        Toast.makeText(CreateUserStory.this, "Your story has been updated", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void updateUserStoryInDatabase(String photoURL, String status) {
        String currentUserId = currentUser.getUid();
        UserStory userStory = new UserStory(currentUserId, photoURL, status);
        userDataReference.child(currentUserId).child("story").setValue(userStory);
    }

    private void referenceViews() {
        imageViewStoryPhoto = findViewById(R.id.imageViewStoryPhoto);
        imageButtonCapturePhoto = findViewById(R.id.imageButtonCapture);
        editTextStatus = findViewById(R.id.editTextStatus);
        buttonUpdate = findViewById(R.id.buttonUpdateStory);
        buttonCancel = findViewById(R.id.buttonCancelStory);
    }

    private void referenceFirebaseInstances() {
        mAuth = FirebaseInstances.getDatabaseAuth();
        currentUser = mAuth.getCurrentUser();
        userDataReference = FirebaseInstances.getDatabaseReference("/users");
        mDataStore = FirebaseInstances.getFirebaseStorage();
        databaseStoreRef = mDataStore.getReferenceFromUrl(STORE_URL);
    }


}
