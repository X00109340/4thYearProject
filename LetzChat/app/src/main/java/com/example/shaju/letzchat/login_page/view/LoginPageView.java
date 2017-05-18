package com.example.shaju.letzchat.login_page.view;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.example.shaju.letzchat.R;

/**
 * Created by Shajun on 15/03/2017.
 */

public class LoginPageView extends CoordinatorLayout implements LoginPageDisplayer {

    private EditText emailEditText;
    private EditText passwordEditText;



    //Different buttons - google, login, forgot, register
    private View googleButton;
    private View loginButton;
    private View forgotButton;
    private View registerButton;

    //Layout - CoordinatorLayout
    private CoordinatorLayout coordinatorLayout;



    public LoginPageView(Context contextIN, AttributeSet attributeSetIN)
    {
        super(contextIN, attributeSetIN);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View.inflate(getContext(), R.layout.merge_login_view, this);

        //Initialise with findViewById
        emailEditText = (EditText) this.findViewById(R.id.emailEditText);
        passwordEditText = (EditText) this.findViewById(R.id.passwordEditText);

        googleButton = this.findViewById(R.id.google);
        loginButton = this.findViewById(R.id.loginButton);
        forgotButton = this.findViewById(R.id.forgotButton);
        registerButton = this.findViewById(R.id.registerButton);

        coordinatorLayout = (CoordinatorLayout) this.findViewById(R.id.activity_login);

    }

    @Override
    public void attach(final LoginPageActionListeners loginPageActionListenersIN)
    {
        loginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                loginPageActionListenersIN.onEmailAndPasswordSelected(emailEditText.getText().toString(),passwordEditText.getText().toString());
            }
        });
        //Google sign in button clicked
        googleButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                loginPageActionListenersIN.onGoogleLoginSelected();
            }
        });
        //register button clicked
        registerButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                loginPageActionListenersIN.onRegistrationButtonSelected();
            }
        });
        //forgot button clicked
        forgotButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                loginPageActionListenersIN.onForgotDialogSelected();
            }
        });
    }

    //Show error message on displayUserDetails (TOAST)
    @Override
    public void showAuthenticationErrorMessage(String messageIN) {
        Snackbar.make(coordinatorLayout, messageIN, Snackbar.LENGTH_LONG).show();
    }

    //Remove on click listeners
    @Override
    public void detach(LoginPageActionListeners loginPageActionListenersIN) {
        googleButton.setOnClickListener(null);
        loginButton.setOnClickListener(null);
    }


    @Override
    public void showErrorFromResourcesString(int idIN) {
        Snackbar.make(coordinatorLayout, getContext().getString(idIN), Snackbar.LENGTH_LONG).show();
    }


}
