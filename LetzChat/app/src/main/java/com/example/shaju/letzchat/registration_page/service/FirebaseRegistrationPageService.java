package com.example.shaju.letzchat.registration_page.service;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func0;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.jakewharton.rxrelay.BehaviorRelay;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import static com.google.android.gms.internal.zzt.TAG;

/**
 * Created by Shajun on 24/02/2017.
 */

public class FirebaseRegistrationPageService implements RegistrationPageService {

    //Firebase authentication
    private FirebaseAuth firebaseAuthentication;

    //To check if registration was completed
    private Boolean isRegistrationCompletedCheck;

    private BehaviorRelay<Boolean> registerBehaviourRelay;

    //Constructor
    public FirebaseRegistrationPageService(FirebaseAuth firebaseAuthenticationIN) {
        firebaseAuthentication = firebaseAuthenticationIN;
        isRegistrationCompletedCheck = false;
        registerBehaviourRelay = BehaviorRelay.create();
    }

    //Registration of user details

    /**
     * Retrieved from: https://firebase.google.com/docs/auth/android/password-auth
     */
    @Override
    public void registerUserEmailAndPassword(String emailIN, String passwordIN) {
        firebaseAuthentication.createUserWithEmailAndPassword(emailIN, passwordIN)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            isRegistrationCompletedCheck = true;
                            Log.d(TAG, "createUserWithEmail:success");
                        }
                        registerBehaviourRelay.call(isRegistrationCompletedCheck);
                    }
                });
    }

    private Observable<Boolean> initRelay() {
        return Observable.defer(new Func0<Observable<Boolean>>() {
            @Override
            public Observable<Boolean> call() {
                if (registerBehaviourRelay.hasValue() && registerBehaviourRelay.getValue()) {
                    return Observable.empty();
                } else {
                    return Observable.create(new Observable.OnSubscribe<Boolean>() {
                        @Override
                        public void call(Subscriber<? super Boolean> subscriber) {
                            subscriber.onCompleted();
                        }
                    });
                }
            }
        });
    }

    //return registration
    @Override
    public Observable<Boolean> getRegistrationPassCheck()
    {
        return registerBehaviourRelay.startWith(initRelay());
    }


}
