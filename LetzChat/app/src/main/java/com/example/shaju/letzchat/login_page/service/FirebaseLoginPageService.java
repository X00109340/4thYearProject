package com.example.shaju.letzchat.login_page.service;

import com.example.shaju.letzchat.login_page.model.LoginAuthModel;
import com.example.shaju.letzchat.main_page.database.CloudMessagingTokenDatabase;
import com.jakewharton.rxrelay.BehaviorRelay;
import com.example.shaju.letzchat.login_page.database.LoginPageAuthDatabase;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func0;

/**
 * Created by Shajun on 14/03/2017.
 */

/**
 * Based on https://github.com/novoda/bonfire-firebase-sample/blob/master/android/core/src/main/java/com/novoda/bonfire/login/service/FirebaseLoginService.java
 * Bonfire login service
 */

public class FirebaseLoginPageService implements LoginPageService {

    //
    private  BehaviorRelay<LoginAuthModel> authRelay;
    //Interface
    private final LoginPageAuthDatabase loginPageAuthDatabase;
    //Token for google sign in
    private final CloudMessagingTokenDatabase cloudMessagingTokenDatabase;


    public FirebaseLoginPageService(LoginPageAuthDatabase loginPageAuthDatabaseIN, CloudMessagingTokenDatabase cloudMessagingTokenDatabaseIN) {
        cloudMessagingTokenDatabase = cloudMessagingTokenDatabaseIN;
        loginPageAuthDatabase = loginPageAuthDatabaseIN;
        authRelay = BehaviorRelay.create();
    }



    //Retrieved from above github link
    @Override
    public Observable<LoginAuthModel> getAuthentication() {
        return authRelay
                .startWith(initRelay());
    }

    //Retrieved from above github link
    private Observable<LoginAuthModel> initRelay() {
        return Observable.defer(new Func0<Observable<LoginAuthModel>>() {
            @Override
            public Observable<LoginAuthModel> call() {
                if (authRelay.hasValue() && authRelay.getValue().isSuccess()) {
                    return Observable.empty();
                } else {
                    return fetchUser();
                }
            }
        });
    }

    //Retrieved from above github link
    private Observable<LoginAuthModel> fetchUser() {
        return loginPageAuthDatabase.readAuthentication()
                .doOnNext(authRelay)
                .ignoreElements();
    }


    //Login with email and password
    @Override
    public void loginWithEmailAndPassword(final String email, final String password) {
        loginPageAuthDatabase.loginWithEmailAndPassword(email,password)
                .subscribe(new Action1<LoginAuthModel>() {
                    @Override
                    public void call(LoginAuthModel loginAuthModel) {
                        if (loginAuthModel.isSuccess()) {
                            cloudMessagingTokenDatabase.enableUserToken(loginAuthModel.getUser().getUid());
                        }
                        authRelay.call(loginAuthModel);
                    }
                });
    }

    //Retrieved from above github link
    @Override
    public void loginWithGoogle(String idTokenIN) {
        loginPageAuthDatabase.loginWithGoogle(idTokenIN)
                .subscribe(new Action1<LoginAuthModel>() {
                    @Override
                    public void call(LoginAuthModel loginAuthModel) {
                        if (loginAuthModel.isSuccess()) {
                            cloudMessagingTokenDatabase.enableUserToken(loginAuthModel.getUser().getUid());
                        }
                        authRelay.call(loginAuthModel);
                    }
                });
    }


    @Override
    public void sendPasswordResetEmailToUser(String email) {
        loginPageAuthDatabase.sendPasswordResetEmailToUser(email);
    }



}