package com.example.shaju.letzchat.navigations;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.shaju.letzchat.registration_page.RegistrationPageActivity;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.example.shaju.letzchat.R;
import com.example.shaju.letzchat.login_page.LoginUsingGoogleApiClient;

import android.content.Intent;
import android.text.InputType;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;



public class AndroidLoginNavigator implements LoginNavigator {

    //Listners
    private LoginResultListener loginResultListener;
    private ForgotDialogListener forgotDialogListener;

    //navigator
    private final Navigator navigator;

    private final AppCompatActivity activity;
    private final LoginUsingGoogleApiClient loginUsingGoogleApiClient;

    public AndroidLoginNavigator(AppCompatActivity appCompatActivityIN, LoginUsingGoogleApiClient loginUsingGoogleApiClientIN, Navigator navigatorIN) {
        loginUsingGoogleApiClient = loginUsingGoogleApiClientIN;
        activity = appCompatActivityIN;
        navigator = navigatorIN;
    }

    //Show forgot dialog
    @Override
    public void showForgotDialog() {
        MaterialDialog dialog = new MaterialDialog.Builder(activity)
                .title(R.string.login_dialog_forgot_title)
                .inputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS)
                .positiveText(R.string.login_dialog_forgot_ok)
                .negativeText(R.string.login_dialog_forgot_close)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (dialog.getInputEditText() != null)
                            forgotDialogListener.onPositiveSelected(dialog.getInputEditText().getText().toString());
                        dialog.dismiss();
                    }
                })
                .input(R.string.login_dialog_forgot_title, 0, false, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {

                    }
                }).show();
    }

    //Go to registration activity
    @Override
    public void toRegistration() {
        activity.startActivity(new Intent(activity, RegistrationPageActivity.class));
    }

    //google plus login
    @Override
    public void toGooglePlusLogin() {
        Intent signInIntent = loginUsingGoogleApiClient.getSignInIntent();
        activity.startActivityForResult(signInIntent, 242);
    }


    @Override
    public void attach(LoginResultListener loginResultListenerIN, ForgotDialogListener forgotDialogListenerIN) {
        this.loginResultListener = loginResultListenerIN;
        this.forgotDialogListener = forgotDialogListenerIN;
    }

    @Override
    public void detach(LoginResultListener loginResultListenerIN, ForgotDialogListener ForgotDialogListenerIN) {
        loginResultListener = null;
        forgotDialogListener = null;
    }

    public boolean onActivityResult(int requestCodeIN, int resultCodeIN, Intent intentIN) {
        if (requestCodeIN != 242)
        {
            return false;
        }
        GoogleSignInResult result = loginUsingGoogleApiClient.getSignInResultFromIntent(intentIN);
        if (result != null && result.isSuccess())
        {
            //if successful
            GoogleSignInAccount account = result.getSignInAccount();
            loginResultListener.onGoogleLoginSuccess(account.getIdToken());
        }
        else
        {
            loginResultListener.onLoginFailed(result.getStatus().getStatusMessage());
        }
        return true;
    }


    @Override
    public void toMainActivity()
    {
        navigator.toMainActivity();
    }

    @Override
    public void toParent() {
        //TO DO
    }

    @Override
    public void toLogin() {
        //TO DO
    }

}
