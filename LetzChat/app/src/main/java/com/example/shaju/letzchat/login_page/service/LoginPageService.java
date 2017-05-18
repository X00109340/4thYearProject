package com.example.shaju.letzchat.login_page.service;


import com.example.shaju.letzchat.login_page.model.LoginAuthModel;

import rx.Observable;

/**
 * Created by Shajun on 14/03/2017.
 */

/**
 * Based on https://github.com/novoda/bonfire-firebase-sample/blob/master/android/core/src/main/java/com/novoda/bonfire/login/service/LoginService.java
 * Bonfire login service
 */

public interface LoginPageService {

    //login with email address
    void loginWithEmailAndPassword(String emailIN, String passwordIN);
    //password reset email
    void sendPasswordResetEmailToUser(String email);
    //
    Observable<LoginAuthModel> getAuthentication();

    //Login with google sign in
    void loginWithGoogle(String idToken);


}
