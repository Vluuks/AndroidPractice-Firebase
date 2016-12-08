package com.example.renske.firebase_het_experiment;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.AuthResult;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Firebase_test";
    private FirebaseAuth authTest;
    private FirebaseAuth.AuthStateListener authListenerTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        authTest = FirebaseAuth.getInstance();
        setListener();
    }

    /* Get user data from EditText fields, verifies and proceeds to account creation. */
    public void registerUser(View view) {

        EditText newUserName = (EditText) findViewById(R.id.editText);
        EditText newUserPw = (EditText) findViewById(R.id.editText2);
        FirebaseAuth auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            Log.d(TAG, "user not null");
        } else {
            Log.d(TAG, "user null");

            // Obtain value of text fields
            String email = newUserName.getText().toString();
            String password = newUserPw.getText().toString();

            // Verify that fields are not empty before creating account
            if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                createNewUser(email, password);
            }
        }

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
                    goToNextActivity();
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

    }

    /* Adds a new user to the FireBase user database with the provided username and password.
    * A check for a valid email adress would probably be a good idea. TODO */
    private void createNewUser(String email, String password){

        Log.d("createNewUser", "started");

        authTest.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.d(TAG, "creating a new user failed");
                            // TODO toast
                        }

                    }
                });

    }

    /* Logs a user in */
    public void loginUser(View view) {
        EditText newUserName = (EditText) findViewById(R.id.editText);
        EditText newUserPw = (EditText) findViewById(R.id.editText2);
        FirebaseAuth auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            Log.d(TAG, "user not null");
        } else {
            Log.d(TAG, "user null");

            // Obtain value of text fields
            String email = newUserName.getText().toString();
            String password = newUserPw.getText().toString();

            // Verify that fields are not empty before logging user in
            if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        goToNextActivity();
                    }

                });
            }
        }
    }

    /* Sends the user to the activity containing account related information */
    private void goToNextActivity(){
        startActivity(new Intent(MainActivity.this, AccountActivity.class));
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

