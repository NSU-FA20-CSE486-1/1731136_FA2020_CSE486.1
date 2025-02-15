package com.ferdouszislam.nsu.cse486.sec01.homemealapp.auth;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/*
Firebase Auth using Email & Password Integration class
 */
public class FirebaseEmailPasswordAuthentication extends Authentication{

    private static final String TAG = "FEPA-debug";

    private FirebaseAuth mFirebaseAuth;
    private EmailPasswordAuthUser mUser;

    public FirebaseEmailPasswordAuthentication(Context context){

        try {
            this.mFirebaseAuth = FirebaseAuth.getInstance();
        } catch (IllegalStateException e){
            FirebaseApp.initializeApp(context);
            this.mFirebaseAuth = FirebaseAuth.getInstance();
        }
        setupMUser();
    }

    public FirebaseEmailPasswordAuthentication() {

        this.mFirebaseAuth = FirebaseAuth.getInstance();
        setupMUser();
    }

    public FirebaseEmailPasswordAuthentication(AuthenticationCallbacks mAuthenticationCallbacks) {
        super(mAuthenticationCallbacks);

        this.mFirebaseAuth = FirebaseAuth.getInstance();
        setupMUser();
    }

    private void setupMUser() {

        this.mUser = new EmailPasswordAuthUser();
        mUser.setmUid(mFirebaseAuth.getUid());

        try {
            mUser.setmEmail(mFirebaseAuth.getCurrentUser().getEmail());
        }catch (NullPointerException e){

            Log.d(TAG, "setupMUser: user was not logged in!");
            
            mUser.setmEmail(null);
        }
    }

    public FirebaseEmailPasswordAuthentication(RegisterUserAuthenticationCallbacks mRegisterUserAuthenticationCallbacks,
                                               EmailPasswordAuthUser mUser) {
        super(mRegisterUserAuthenticationCallbacks);

        mFirebaseAuth = FirebaseAuth.getInstance();
        this.mUser = mUser;
    }

    public FirebaseEmailPasswordAuthentication(AuthenticationCallbacks mAuthenticationCallbacks, EmailPasswordAuthUser mUser) {
        super(mAuthenticationCallbacks);

        mFirebaseAuth = FirebaseAuth.getInstance();
        this.mUser = mUser;
    }

    @Override
    public void registerUserAuthentication() {

        if(mFirebaseAuth==null){
            mRegisterUserAuthenticationCallbacks.onRegistrationFailure("firebaseAuth is null");
            return;
        }

        if(mUser==null){
            mRegisterUserAuthenticationCallbacks.onRegistrationFailure("auth user is null");
            return;
        }

        mFirebaseAuth.createUserWithEmailAndPassword(mUser.getmEmail(), mUser.getmPassword())
                .addOnCompleteListener(task -> {

                    if(task.isSuccessful()){

                        mUser.setmUid(mFirebaseAuth.getUid());

                        // send the uid
                        mRegisterUserAuthenticationCallbacks.onRegistrationSuccess(mUser);

                        Log.d(TAG, "onComplete: firebase auth user registration success");
                    }

                    else{

                        mRegisterUserAuthenticationCallbacks.onRegistrationFailure("firebase auth create user failed");
                        Log.d(TAG, "onComplete: firebase auth create user failed");
                    }
                });
    }

    @Override
    public void authenticateUser() {

        if(mFirebaseAuth==null){
            mAuthenticationCallbacks.onAuthenticationFailure("firebaseAuth is null");
            return;
        }

        if(mFirebaseAuth.getCurrentUser()!=null) {

            mUser.setmUid(mFirebaseAuth.getUid());
            mAuthenticationCallbacks.onAuthenticationSuccess(mUser);

            Log.d(TAG, "authenticateUser: firebaseAuthUser not null, user already logged in");

            return;
        }

        // user not logged in need to authenticate credentials
        if(mUser==null || mUser.getmEmail()==null || mUser.getmPassword()==null){
            mAuthenticationCallbacks.onAuthenticationFailure("auth user is null");
            return;
        }

        mFirebaseAuth.signInWithEmailAndPassword(mUser.getmEmail(), mUser.getmPassword())
                .addOnCompleteListener(task -> {

                    if(task.isSuccessful()){

                        mUser.setmUid(mFirebaseAuth.getUid());
                        mAuthenticationCallbacks.onAuthenticationSuccess(mUser);

                        Log.d(TAG, "onComplete: firebase auth user login success");
                    }

                    else{

                        mAuthenticationCallbacks.onAuthenticationFailure("firebase auth user login failed");
                        Log.d(TAG, "onComplete: firebase auth user login failed");
                    }
                });
    }

    @Override
    public void signOut() {
        mFirebaseAuth.signOut();
    }

    public EmailPasswordAuthUser getmUser() {
        return mUser;
    }

    public void setmUser(EmailPasswordAuthUser mUser) {
        this.mUser = mUser;
    }
}
