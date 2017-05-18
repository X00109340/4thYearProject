package com.example.shaju.letzchat.registration_page.view;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.shaju.letzchat.R;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.support.design.widget.CoordinatorLayout;

/**
 * Created by Shajun on 27/02/2017.
 */

public class RegistrationPageView extends CoordinatorLayout implements RegistrationPageDisplayer {
    //Go back to login button
    private View loginButton;

    //Email edit text
    private EditText emailEditText;
    //Password edit text
    private EditText passwordEditText;
    //Confirmation password edit text
    private EditText passwordConfirmEditText;
    //Registration Button
    private View registrationButton;

    //Layout
    private CoordinatorLayout coordinatorLayout;
    //Dialog box
    private MaterialDialog dialog;

    public RegistrationPageView(Context contextIN, AttributeSet attributeSetIN) {
        super(contextIN, attributeSetIN);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        //Set the view
        View.inflate(getContext(), R.layout.merge_registration_view, this);

        //Set the layout
        coordinatorLayout = (CoordinatorLayout) this.findViewById(R.id.layout);

        loginButton = this.findViewById(R.id.loginButton);

        passwordEditText = (EditText) this.findViewById(R.id.passwordEditText);
        passwordConfirmEditText = (EditText) this.findViewById(R.id.confirmEditText);
        emailEditText = (EditText) this.findViewById(R.id.emailEditText);

        registrationButton = this.findViewById(R.id.registerButton);

        dialog = new MaterialDialog.Builder(getContext())
                .title("Welcome To LetzChat")
                .content("Next please enter your Online ID and also register your phone number")
                .positiveText("CLOSE")
                .build();
    }


    @Override
    public void showRegistrationAlertDialog(int stringID)
    {
        dialog.show();
    }

    @Override
    public void showErrorFromResourcesString(int stringID)
    {
        Snackbar.make(coordinatorLayout, getContext().getString(stringID), Snackbar.LENGTH_LONG).show();
    }

    //Attach the listners for the buttons and alert dialog
    @Override
    public void attach(final RegistrationPageClickActionListener actionListener) {
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                actionListener.onAlertDialogDismissed();
            }
        });

        /**
         * Retrieved from: https://firebase.google.com/docs/auth/android/password-auth
         */
        registrationButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                actionListener.registerButtonClick(
                        emailEditText.getText().toString(),
                        passwordEditText.getText().toString(),
                        passwordConfirmEditText.getText().toString()
                );
            }
        });

        loginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                actionListener.loginButtonSelected();
            }
        });

    }

    //Remove the listeners
    @Override
    public void detach(RegistrationPageClickActionListener actionListener) {
        dialog.setOnDismissListener(null);
        registrationButton.setOnClickListener(null);
        loginButton.setOnClickListener(null);
    }

}
