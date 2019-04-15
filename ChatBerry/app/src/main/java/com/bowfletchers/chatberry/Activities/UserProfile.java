package com.bowfletchers.chatberry.Activities;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.bowfletchers.chatberry.ClassLibrary.FirebaseInstances;
import com.bowfletchers.chatberry.ClassLibrary.Member;
import com.bowfletchers.chatberry.R;
import com.bowfletchers.chatberry.ViewModel.UserData.UserInfoViewModel;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class UserProfile extends AppCompatActivity {

    private final int REQUEST_CODE_IMAGE = 1;
    private final String STORE_URL = "gs://chatberry-201de.appspot.com";
    private final String DEFAULT_PHOTO_URL = "https://cdn.pixabay.com/photo/2016/08/08/09/17/avatar-1577909__340.png";
    private int userOnlineStatus;
    private String userId;

    ImageView imageViewUserPhoto;
    TextView editTextUserName;
    TextView textViewUserEmail;
    Switch aSwitchOnlineStatus;
    Button buttonDone;
    Button buttonSignout;

    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    DatabaseReference mUserRef;
    FirebaseStorage firebaseStore;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        referenceViews();
        // init Fire auth instance
        referenceFirebaseInstances();

        setTitle(currentUser.getDisplayName() + "'s profile");
        displayDefaultUserInfo();

        // open camera when user click on image view
        imageViewUserPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, REQUEST_CODE_IMAGE);
            }
        });

        // handle update user info when click Done button
        buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // save user photo to firebase store
                uploadUserPhotoToStore();
            }
        });

        // sign out user when click sign out button
        buttonSignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // sign out user from mAuth
                mAuth.signOut();
                // set online status to offline (0)
                updateUserOnlineStatus(0);
                Intent backSignInIntent = new Intent(UserProfile.this, LoginAccount.class);
                startActivity(backSignInIntent);
            }
        });

        // update user online status when the switch adjusting
        aSwitchOnlineStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    // when Switch is ON
                    // set online user to 1
                    userOnlineStatus = 1;
                } else {
                    // when Switch is ON
                    // set online user to 0
                    userOnlineStatus = 0;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.homePage:
                Intent chatListIntent = new Intent(UserProfile.this, ChatHistoryList.class);
                startActivity(chatListIntent);
                return true;
            case R.id.my_friends:
                Intent userFriendsIntent = new Intent(UserProfile.this, FriendList.class);
                startActivity(userFriendsIntent);
                return true;
            case R.id.createStory:
                Intent createNewStoryIntent = new Intent(UserProfile.this, CreateUserStory.class);
                startActivity(createNewStoryIntent);
                return true;
            case R.id.friendStories:
                Intent friendStoriesIntent = new Intent(UserProfile.this, FriendStories.class);
                startActivity(friendStoriesIntent);
                return true;
            case R.id.my_friend_requests:
                Intent friendRequestIntent = new Intent(UserProfile.this, FriendRequests.class);
                startActivity(friendRequestIntent);
                return true;
            case R.id.newgc:
                Intent newGC = new Intent(UserProfile.this, NewGroupChat.class);
                startActivity(newGC);
                return true;
            default:
                // Do nothing
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // get the captured image to image view
        if (requestCode == REQUEST_CODE_IMAGE && resultCode == RESULT_OK && data != null) {
            Bitmap bitmapImg = (Bitmap) data.getExtras().get("data");
            imageViewUserPhoto.setImageBitmap(bitmapImg);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void displayDefaultUserInfo(){
          // retrieve user data from database based on id
          mUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
              @Override
              public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                  Log.d("FFF", dataSnapshot.getValue().toString());
                  String userName = dataSnapshot.child("name").getValue().toString();
                  String userEmail = dataSnapshot.child("email").getValue().toString();
                  String userPhotoURL = dataSnapshot.child("profilePicture").getValue().toString();
                  String userOnlineStatus = dataSnapshot.child("onlineStatus").getValue().toString();

                  // mapping data to views
                  editTextUserName.setText(userName);
                  textViewUserEmail.setText(userEmail);
                  Glide.with(UserProfile.this)
                          .load(userPhotoURL)
                          .placeholder(Drawable.createFromPath(DEFAULT_PHOTO_URL))
                          .into(imageViewUserPhoto);
                  if (userOnlineStatus.equals("1")) {
                      aSwitchOnlineStatus.setChecked(true);
                  } else {
                      aSwitchOnlineStatus.setChecked(false);
                  }
              }

              @Override
              public void onCancelled(@NonNull DatabaseError databaseError) {

              }
          });
    }

    private void uploadUserPhotoToStore() {
        // set components to create img name
        String userName = currentUser.getDisplayName();
        String imageName = userName + userId + ".png";

        // upload current img to store
        final StorageReference userImageRef = storageReference.child("images/" + imageName);
        imageViewUserPhoto.setDrawingCacheEnabled(true);
        imageViewUserPhoto.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) imageViewUserPhoto.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = userImageRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(UserProfile.this, "Upload image failed", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // get user img download url from store
                userImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String downloadUrl = uri.toString();
                        // after upload img succeed , update the user profile
                        // with user photo url and user name in Authentication
                        String userName = editTextUserName.getText().toString();
                        updateUserProfileInfo(downloadUrl, userName);

                        // also update user info in database al well
                        updateUserInfoInDatabase(downloadUrl, userName);

                        // update online status
                        updateUserOnlineStatus(userOnlineStatus);

                        // show the upload status to user
                        Toast.makeText(UserProfile.this, "Your profile has been updated.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void updateUserProfileInfo(String newUserPhoToURL, String newUserDisplayName) {
        UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder()
                .setDisplayName(newUserDisplayName)
                .setPhotoUri(Uri.parse(newUserPhoToURL))
                .build();
        currentUser.updateProfile(userProfileChangeRequest)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(UserProfile.this, "User info has been updated successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(UserProfile.this, "Update user information failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void updateUserInfoInDatabase(String newUserPhoToURL, String newUserDisplayName) {
        mUserRef.child("name").setValue(newUserDisplayName);
        mUserRef.child("profilePicture").setValue(newUserPhoToURL);
    }

    private void updateUserOnlineStatus(int userOnlineStatus) {
        mUserRef.child("onlineStatus").setValue(userOnlineStatus);
    }

    private void referenceFirebaseInstances() {
        mAuth = FirebaseInstances.getDatabaseAuth();
        currentUser = mAuth.getCurrentUser();
        userId = currentUser.getUid();
        mUserRef = FirebaseInstances.getDatabaseReference("/users/" + userId);
        firebaseStore = FirebaseInstances.getFirebaseStorage();
        storageReference = firebaseStore.getReferenceFromUrl(STORE_URL);
    }

    private void referenceViews() {
        imageViewUserPhoto = findViewById(R.id.user_profile_avatar);
        editTextUserName = findViewById(R.id.user_profile_name);
        textViewUserEmail = findViewById(R.id.textViewUserProfileEmail);
        aSwitchOnlineStatus = findViewById(R.id.user_profile_onlineStatus_switch);
        buttonDone = findViewById(R.id.user_profile_button_done);
        buttonSignout = findViewById(R.id.user_profile_button_logout);
    }
}
