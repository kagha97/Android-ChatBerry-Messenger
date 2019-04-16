package com.bowfletchers.chatberry.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.bowfletchers.chatberry.ClassLibrary.FirebaseInstances;
import com.bowfletchers.chatberry.ClassLibrary.Member;
import com.bowfletchers.chatberry.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterAccount extends AppCompatActivity {

    EditText editTextEmail;
    EditText editTextUserName;
    EditText editTextPassword;
    EditText editTextConfirmPassword;

    FirebaseAuth mAuthentication;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_account);
        // reference for the views
        referenceUIs();
        // reference for firebase modules
        referenceFirebase();
    }

    public void cancelRegister(View view) {
        // navigate back to welcome page
        Intent backToSignInIntent = new Intent(RegisterAccount.this, WelcomePage.class);
        startActivity(backToSignInIntent);
    }

    public void confirmRegister(View view) {
        // Get user inputs
        final String emailInput = editTextEmail.getText().toString();
        final String usernameInput = editTextUserName.getText().toString();
        String passwordInput = editTextPassword.getText().toString();
        String passwordConfirmInput = editTextConfirmPassword.getText().toString();

        if (emailInput.equals("") || usernameInput.equals("") || passwordInput.equals("")
        || passwordConfirmInput.equals("")) {
            Toast.makeText(this, "Please fill all the inputs", Toast.LENGTH_SHORT).show();
            return;
        }

        // check if the password and confirm password matches
        if (passwordInput.equals(passwordConfirmInput)) {
            // using email and password to sign up
            mAuthentication.createUserWithEmailAndPassword(emailInput, passwordInput)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        //Toast.makeText(RegisterAccount.this, "Register successful", Toast.LENGTH_SHORT).show();
                        // get userID from firebase auth
                        final FirebaseUser currentUser = mAuthentication.getCurrentUser();
                        if (currentUser != null) {

                            // save display name to user profile
                            UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(usernameInput)
                                    .build();
                            currentUser.updateProfile(profileUpdate);

                            String userId = currentUser.getUid();

                            // create user object
                            Member newMember = new Member(userId, usernameInput, emailInput, "");

                            // save new user to firebase database
                            mDatabase.child("users").child(userId).setValue(newMember);

                            // navigate to chat list activity
                            Intent chatListIntent = new Intent(RegisterAccount.this, ChatHistoryList.class);
                            chatListIntent.putExtra("NewUser", newMember);
                            startActivity(chatListIntent);
                        }

                    } else {
                        Toast.makeText(RegisterAccount.this, "Registered failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else {
            Toast.makeText(this, "Password does match.", Toast.LENGTH_SHORT).show();
        }
    }

    private void referenceUIs(){
        editTextEmail = findViewById(R.id.register_email_text);
        editTextUserName = findViewById(R.id.register_username_text);
        editTextPassword = findViewById(R.id.register_password_text);
        editTextConfirmPassword = findViewById(R.id.register_confirm_password_text);
    }

    private void referenceFirebase() {
        mAuthentication = FirebaseInstances.getDatabaseAuth();
        mDatabase = FirebaseInstances.getDatabaseReference("");
    }
}
