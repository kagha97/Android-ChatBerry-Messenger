package com.bowfletchers.chatberry.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.bowfletchers.chatberry.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Login_account extends AppCompatActivity {
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
    }

    public void confirmSingIn(View view) {
        validateUser();
    }

    public void validateUser() {
        editTextEmail = findViewById(R.id.emailInput);
        editTextPassword = findViewById(R.id.passwordInput);
        userInputEmail = editTextEmail.getText().toString();
        userInputPassword = editTextPassword.getText().toString();
        mAuthentication = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        if(!userInputEmail.equals("") && !userInputPassword.equals("")) {
            mAuthentication.signInWithEmailAndPassword(userInputEmail, userInputPassword)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("TAG", "signInWithEmail:success");
                                //FirebaseUser user = mAuthentication.getCurrentUser();
                                Toast.makeText(Login_account.this, "Successfull", Toast.LENGTH_LONG).show();
                                startActivity();
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("TAG", "signInWithEmail:failure", task.getException());
                                Toast.makeText(Login_account.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        else{
            Toast.makeText(Login_account.this , "Please dont leave the fields empty" , Toast.LENGTH_LONG).show();
        }
    }

    public void startActivity() {
        Intent goToIntent = new Intent(Login_account.this, Friend_List.class);
        startActivity(goToIntent);
    }

    public void cancelSignIn(View view) {
        // navigate user to the welcome page
        Intent backToWelcomePage = new Intent(Login_account.this, WelcomePage.class);
        startActivity(backToWelcomePage);
    }
}
