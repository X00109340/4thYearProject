package com.example.shaju.letzchat.login_page.database;

import rx.Observable;

import com.example.shaju.letzchat.login_page.model.LoginAuthModel;


public interface LoginPageAuthDatabase {

    //Login with email and password - basic login
    Observable<LoginAuthModel> loginWithEmailAndPassword(String emailAddressIN, String passwordIN);

    //To reset password if forgotten
    void sendPasswordResetEmailToUser(String emailIN);

    //Check if user is already logged in
    Observable<LoginAuthModel> readAuthentication();

    //TO login with google authentication
    Observable<LoginAuthModel> loginWithGoogle(String idTokenIN);



}
