package com.example.shaju.letzchat.login_page.presenter;


import com.example.shaju.letzchat.login_page.model.LoginAuthModel;
import com.example.shaju.letzchat.login_page.service.LoginPageService;
import com.example.shaju.letzchat.login_page.view.LoginPageDisplayer;
import com.example.shaju.letzchat.R;
import com.example.shaju.letzchat.navigations.LoginNavigator;

import rx.Subscription;

import rx.Subscriber;

/**
 * Based on https://github.com/novoda/bonfire-firebase-sample/blob/master/android/core/src/main/java/com/novoda/bonfire/login/presenter/LoginPresenter.java
 * Bonfire login service
 */

public class LoginPagePresenter {

    //Navigator
    private final LoginNavigator navigator;
    //Service for login
    private final LoginPageService loginPageService;
    //Login displayer
    private final LoginPageDisplayer loginPageDisplayer;


    private Subscription subscription;

    //Constructor
    public LoginPagePresenter(LoginPageService loginPageServiceIN, LoginPageDisplayer loginPageDisplayerIN, LoginNavigator navigatorIN) {
        loginPageService = loginPageServiceIN;
        navigator = navigatorIN;
        loginPageDisplayer = loginPageDisplayerIN;
    }

    /**
     * Based on https://github.com/novoda/bonfire-firebase-sample/blob/master/android/core/src/main/java/com/novoda/bonfire/login/presenter/LoginPresenter.java
     */
    public void startPresenting() {
        navigator.attach(loginResultListener,forgotDialogListener);
        loginPageDisplayer.attach(actionListener);
        subscription = loginPageService.getAuthentication()
                .subscribe(new Subscriber<LoginAuthModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(LoginAuthModel loginAuthModel) {
                        if (loginAuthModel.isSuccess())
                        {
                            navigator.toMainActivity();
                        } else
                        {
                            loginPageDisplayer.showAuthenticationErrorMessage(loginAuthModel.getFailure().getLocalizedMessage());
                        }
                    }
                });
    }

    public void stopPresenting() {
        navigator.detach(loginResultListener,forgotDialogListener);
        loginPageDisplayer.detach(actionListener);
        subscription.unsubscribe();
    }



    /**
     * Based on https://github.com/novoda/bonfire-firebase-sample/blob/master/android/core/src/main/java/com/novoda/bonfire/login/presenter/LoginPresenter.java
     */
    private final LoginNavigator.LoginResultListener loginResultListener = new LoginNavigator.LoginResultListener() {

        @Override
        public void onGoogleLoginSuccess(String tokenIdIN)
        {
            loginPageService.loginWithGoogle(tokenIdIN);
        }

        @Override
        public void onLoginFailed(String statusMessage) {
            loginPageDisplayer.showAuthenticationErrorMessage(statusMessage);
        }
    };

    /**
     * Based on https://github.com/novoda/bonfire-firebase-sample/blob/master/android/core/src/main/java/com/novoda/bonfire/login/presenter/LoginPresenter.java
     */
    private final LoginPageDisplayer.LoginPageActionListeners actionListener = new LoginPageDisplayer.LoginPageActionListeners() {

        @Override
        public void onGoogleLoginSelected() {
            navigator.toGooglePlusLogin();
        }

        @Override
        public void onEmailAndPasswordSelected(String email, String password) {
            if (email.length() == 0) {
                loginPageDisplayer.showErrorFromResourcesString(R.string.login_snackbar_email_short);
                return;
            }
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                loginPageDisplayer.showErrorFromResourcesString(R.string.login_snackbar_email_error);
                return;
            }
            if (password.length() == 0) {
                loginPageDisplayer.showErrorFromResourcesString(R.string.login_snackbar_password_short);
                return;
            }
            loginPageService.loginWithEmailAndPassword(email, password);
        }

        @Override
        public void onRegistrationButtonSelected() {
            navigator.toRegistration();
        }

        @Override
        public void onForgotDialogSelected() {
            navigator.showForgotDialog();
        }

    };

    private final LoginNavigator.ForgotDialogListener forgotDialogListener = new LoginNavigator.ForgotDialogListener() {

        @Override
        public void onPositiveSelected(String email) {
            loginPageService.sendPasswordResetEmailToUser(email);
        }

    };


}

