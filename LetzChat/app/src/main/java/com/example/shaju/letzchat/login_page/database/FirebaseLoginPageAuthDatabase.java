package com.example.shaju.letzchat.login_page.database;

import com.example.shaju.letzchat.login_page.model.LoginAuthModel;
import com.example.shaju.letzchat.users_page.model.User;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.AuthCredential;

import rx.Observable;
import rx.Subscriber;
import android.net.Uri;
import android.support.annotation.NonNull;


/**
 * Created by Shajun on 13/03/2017.
 */

public class FirebaseLoginPageAuthDatabase implements LoginPageAuthDatabase {

        //Firebase authentication
        private final FirebaseAuth firebaseAuthentication;

    //constructor
        public FirebaseLoginPageAuthDatabase(FirebaseAuth firebaseAuthenticationIN) {
            firebaseAuthentication = firebaseAuthenticationIN;
        }


        // Retrieved from https://firebase.google.com/docs/auth/android/google-signin
        @Override
        public Observable<LoginAuthModel> loginWithGoogle(final String idTokenIN) {
            return Observable.create(new Observable.OnSubscribe<LoginAuthModel>() {
                @Override
                public void call(final Subscriber<? super LoginAuthModel> subscriber) {
                    AuthCredential authCredential = GoogleAuthProvider.getCredential(idTokenIN, null);
                    firebaseAuthentication.signInWithCredential(authCredential)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser firebaseUser = task.getResult().getUser();
                                        subscriber.onNext(authenticationFrom(firebaseUser));
                                    } else {
                                        subscriber.onNext(new LoginAuthModel(task.getException()));
                                    }
                                    subscriber.onCompleted();
                                }
                            });
                }
            });
        }

        //Retrieved from https://firebase.google.com/docs/auth/android/password-auth
        @Override
        public Observable<LoginAuthModel> loginWithEmailAndPassword(final String emailAddressIN, final String passwordIN) {
            return Observable.create(new Observable.OnSubscribe<LoginAuthModel>() {
                @Override
                public void call(final Subscriber<? super LoginAuthModel> subscriber) {
                    firebaseAuthentication.signInWithEmailAndPassword(emailAddressIN, passwordIN)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser firebaseUser = task.getResult().getUser();
                                        subscriber.onNext(authenticationFrom(firebaseUser));
                                    } else {
                                        subscriber.onNext(new LoginAuthModel(task.getException()));
                                    }
                                    subscriber.onCompleted();
                                }
                            });
                }
            });
        }
    //Retrieved from https://firebase.google.com/docs/auth/android/manage-users
    @Override
    public void sendPasswordResetEmailToUser(String emailIN) {
        firebaseAuthentication.sendPasswordResetEmail(emailIN)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            return;
                        }
                    }
                });
    }

    //to check if user is already logged in
    @Override
    public Observable<LoginAuthModel> readAuthentication() {
        return Observable.create(new Observable.OnSubscribe<LoginAuthModel>() {
            @Override
            public void call(Subscriber<? super LoginAuthModel> subscriber) {
                FirebaseUser user = firebaseAuthentication.getCurrentUser();
                if (user != null) {
                    //if user is logged in then....
                    subscriber.onNext(authenticationFrom(user));
                }
                subscriber.onCompleted();
            }
        });
    }

    private LoginAuthModel authenticationFrom(FirebaseUser firebaseUserIN) {
        //Profile image uri
        Uri profileImageUri = firebaseUserIN.getPhotoUrl();
        //return user details to authentication
        return new LoginAuthModel(new User(firebaseUserIN.getUid(), firebaseUserIN.getDisplayName(), profileImageUri == null ? "" : profileImageUri.toString(), firebaseUserIN.getEmail()));
    }

}