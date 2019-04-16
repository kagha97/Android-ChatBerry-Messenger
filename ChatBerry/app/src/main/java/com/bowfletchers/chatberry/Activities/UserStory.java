package com.bowfletchers.chatberry.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
import android.widget.Toast;

import com.bowfletchers.chatberry.ClassLibrary.FirebaseInstances;
import com.bowfletchers.chatberry.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class UserStory extends AppCompatActivity {

    private final int REQUEST_CODE_IMAGE = 1;
    private final String STORE_URL = "gs://chatberry-201de.appspot.com";
    private final String DEFAULT_PHOTO_STORY = "http://www.pngpix.com/wp-content/uploads/2016/07/PNGPIX-COM-Blossom-Flower-PNG-Transparent-Image-500x514.png";
    private String currentUserId;


    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference userStoryReference;
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
        displayUserStory();

        // handle cancel button
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goBackHomeIntent = new Intent(UserStory.this, ChatHistoryList.class);
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
                if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(cameraIntent, REQUEST_CODE_IMAGE);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // get the captured image to image view
        if (requestCode == REQUEST_CODE_IMAGE && resultCode == RESULT_OK && data != null) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageViewStoryPhoto.setImageBitmap(imageBitmap);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_only_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.homePageOnly:
                Intent intent = new Intent(this, ChatHistoryList.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                this.startActivity(intent);
                Runtime.getRuntime().exit(0);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void displayUserStory() {
        setTitle(currentUser.getDisplayName() + "'s story");
        userStoryReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String userStoryPhoto = dataSnapshot.child("photoStoryURL").getValue().toString();
                String userStatusMsg = dataSnapshot.child("statusMessage").getValue().toString();

                // mapping user story data to views
                Glide.with(UserStory.this)
                        .load(userStoryPhoto)
                        .placeholder(Drawable.createFromPath(DEFAULT_PHOTO_STORY))
                        .into(imageViewStoryPhoto);
                editTextStatus.setText(userStatusMsg);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
                Toast.makeText(UserStory.this, "Upload story image failed", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(UserStory.this, "Your story has been updated", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void updateUserStoryInDatabase(String photoURL, String status) {
        String currentUserId = currentUser.getUid();
        String currentUserName = currentUser.getDisplayName();
        com.bowfletchers.chatberry.ClassLibrary.UserStory userStory = new com.bowfletchers.chatberry.ClassLibrary.UserStory(currentUserId, currentUserName, photoURL, status);
        userStoryReference.setValue(userStory);
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
        currentUserId = currentUser.getUid();
        userStoryReference = FirebaseInstances.getDatabaseReference("/users/" + currentUserId + "/story");
        mDataStore = FirebaseInstances.getFirebaseStorage();
        databaseStoreRef = mDataStore.getReferenceFromUrl(STORE_URL);
    }
}