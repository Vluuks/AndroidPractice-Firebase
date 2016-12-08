package com.example.renske.firebase_het_experiment;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.AuthResult;

public class MainActivity extends AppCompatActivity {


    private FirebaseAuth authTest;
    private FirebaseAuth.AuthStateListener authListenerTest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        FirebaseAuth auth = FirebaseAuth.getInstance();
//        if (auth.getCurrentUser() != null) {
//            // already signed in
//        } else {
//            // not signed in
//        }


        authTest = FirebaseAuth.getInstance();
        setListener();


    }



    public void getUserData(View view) {

        EditText newUserName = (EditText) findViewById(R.id.editText);


    }


    private void setListener(){

        authListenerTest = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    // User is signed in
                    Log.d("test", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("test", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

    }


    private void signUserOut(){

        FirebaseAuth.getInstance().signOut();

    }


    private void createNewUser(String email, String password){

        authTest.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("test", "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.d("HOI", "DOEI failed");
                        }

                        // ...
                    }
                });

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

