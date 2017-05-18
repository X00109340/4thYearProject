package com.example.shaju.letzchat.navigations;

/**
 * Based on from https://github.com/novoda/bonfire-firebase-sample/blob/master/android/core/src/main/java/com/novoda/bonfire/navigation/LoginNavigator.java
 */

public interface LoginNavigator extends Navigator {

    void attach(LoginResultListener loginResultListener, ForgotDialogListener forgotDialogListener);

    void detach(LoginResultListener loginResultListener, ForgotDialogListener forgotDialogListener);

    void toGooglePlusLogin();

    void showForgotDialog();

    void toRegistration();


    interface ForgotDialogListener {

        void onPositiveSelected(String email);

    }

    interface LoginResultListener {

        void onGoogleLoginSuccess(String tokenId);

        void onLoginFailed(String statusMessage);

    }


}
