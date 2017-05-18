package com.example.shaju.letzchat.login_page.view;

/**
 * Created by Shajun on 14/03/2017.
 */

/**
 * Based on https://github.com/novoda/bonfire-firebase-sample/blob/master/android/core/src/main/java/com/novoda/bonfire/login/displayer/LoginDisplayer.java
 * Login Displayer from Bonfire firebase example
 */

public interface LoginPageDisplayer {

    void attach(LoginPageActionListeners loginPageActionListenersIN);

    void detach(LoginPageActionListeners loginPageActionListenersIN);

    void showAuthenticationErrorMessage(String messageIN);


    //Listeners for button clicks
    public interface LoginPageActionListeners {
        //Login with email and password
        void onEmailAndPasswordSelected(String emailIN, String passwordIN);
        //Google sign in
        void onGoogleLoginSelected();
        //Resgister button selected
        void onRegistrationButtonSelected();
        //Forgot password selected
        void onForgotDialogSelected();

    }

    void showErrorFromResourcesString(int idIN);

}