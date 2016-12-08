package com.example.renske.firebase_het_experiment;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AccountActivity extends AppCompatActivity {


    private FirebaseAuth authTest;
    private FirebaseAuth.AuthStateListener authListenerTest;
    private static final String TAG = "Firebase_logout_test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        authTest = FirebaseAuth.getInstance();
        setListener();
    }

    /* Sets the authstate listener. This checks whether there is still a user logged in at a
     * given moment, preventing non-logged in users from seeing paged they shouldn't! */
    private void setListener(){

        authListenerTest = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    goToRegisterLoginActivity();
                }
                // ...
            }
        };

    }

    /* Sends the user back to the starting activity to log in or register */
    private void goToRegisterLoginActivity(){
        startActivity(new Intent(AccountActivity.this, MainActivity.class));
    }

    /* Logs the user out */
    public void logoutUser(View view) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            Log.d(TAG, "user not null, proceeding to log out");
            auth.signOut();
            goToRegisterLoginActivity();
        }
    }

    /* Lifecycle methods */
    @Override
    public void onStart() {
        super.onStart();
        authTest.addAuthStateListener(authListenerTest);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListenerTest != null) {
            authTest.removeAuthStateListener(authListenerTest);
        }
    }
}
