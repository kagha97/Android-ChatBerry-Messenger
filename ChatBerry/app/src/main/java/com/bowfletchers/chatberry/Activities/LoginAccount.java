package com.bowfletchers.chatberry.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.bowfletchers.chatberry.ClassLibrary.FirebaseInstances;
import com.bowfletchers.chatberry.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginAccount extends AppCompatActivity {
    EditText editTextEmail;
    EditText editTextPassword;
    String userInputEmail;
    String userInputPassword;
    FirebaseAuth mAuthentication;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_account);

        referenceUIs();
        referenceFirebase();
    }

    public void confirmSingIn(View view) {
        validateUser();
    }

    private void validateUser() {

        // get user inputs for email and password
        userInputEmail = editTextEmail.getText().toString();
        userInputPassword = editTextPassword.getText().toString();

        // authenticate user credentials using fire base method
        mAuthentication.signInWithEmailAndPassword(userInputEmail, userInputPassword)
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // log in success
                        // get userId of log-in user
                        FirebaseUser logInUser = mAuthentication.getCurrentUser();
                        String loginEmail = logInUser.getEmail();
                        String loginUserId = logInUser.getUid();
                        navigateToChatList(loginEmail, loginUserId);
                    }
                    else {
                        // log in failed
                        Toast.makeText(LoginAccount.this, "Log in failed :(((", Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }

    public void navigateToChatList(String userEmail, String userId) {
        Intent goToIntent = new Intent(LoginAccount.this, ChatHistoryList.class);
        startActivity(goToIntent);
        finish();
    }

    public void cancelSignIn(View view) {
        // navigate user to the welcome page
        Intent backToWelcomePage = new Intent(LoginAccount.this, WelcomePage.class);
        startActivity(backToWelcomePage);
    }

    private void referenceUIs() {
        editTextEmail = findViewById(R.id.emailInput);
        editTextPassword = findViewById(R.id.passwordInput);
    }

    private void referenceFirebase() {
        mAuthentication = FirebaseInstances.getDatabaseAuth();
        mDatabase = FirebaseInstances.getDatabaseReference("");
    }
}
