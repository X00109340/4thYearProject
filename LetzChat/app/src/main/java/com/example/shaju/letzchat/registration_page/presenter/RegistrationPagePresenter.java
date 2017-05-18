package com.example.shaju.letzchat.registration_page.presenter;

import com.example.shaju.letzchat.navigations.RegistrationNavigator;
import com.example.shaju.letzchat.registration_page.service.RegistrationPageService;
import com.example.shaju.letzchat.R;
import com.example.shaju.letzchat.registration_page.view.RegistrationPageDisplayer;

import rx.functions.Action1;
import rx.Subscription;

/**
 * Created by Shajun on 23/02/2017.
 */

public class RegistrationPagePresenter {

    //Registration navigator
    private final RegistrationNavigator navigator;
    //Serice for registering a new user
    private final RegistrationPageService registrationPageService;
    //Display registration page
    private final RegistrationPageDisplayer registrationPageDisplayer;

    //Subscription
    private Subscription subscription;

    //Constructor
    public RegistrationPagePresenter(RegistrationPageService registrationPageServiceIN, RegistrationPageDisplayer registrationPageDisplayerIN, RegistrationNavigator navigatorIN) {
        registrationPageService = registrationPageServiceIN;
        navigator = navigatorIN;
        registrationPageDisplayer = registrationPageDisplayerIN;
    }


    private final RegistrationPageDisplayer.RegistrationPageClickActionListener actionListener = new RegistrationPageDisplayer.RegistrationPageClickActionListener() {

        //Alert dialog dismissed
        @Override
        public void onAlertDialogDismissed() {
            navigator.toLogin();
        }

        //Login text selected
        @Override
        public void loginButtonSelected() {
            navigator.toLogin();
        }


        @Override
        public void registerButtonClick(String emailIN, String passwordIN, String confirmIN) {
            //Check to see if the input email matches the email structure
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailIN).matches()) {
                registrationPageDisplayer.showErrorFromResourcesString(R.string.registration_snackbar_email_error);
                return;
            }

            //Check to see if the email length is 0
            if (emailIN.length() == 0) {
                registrationPageDisplayer.showErrorFromResourcesString(R.string.registration_snackbar_email_short);
                return;
            }

            //Check to see password length
            if (passwordIN.length() < 8) {
                registrationPageDisplayer.showErrorFromResourcesString(R.string.registration_snackbar_password_short);
                return;
            }
            //If confirmation both passwords do not match
            if (!confirmIN.equals(passwordIN)) {
                registrationPageDisplayer.showErrorFromResourcesString(R.string.registration_snackbar_password_error);
                return;
            }

            registrationPageService.registerUserEmailAndPassword(emailIN,passwordIN);
        }


    };

    //Start presenting
    public void startPresenting() {
        //attach the listeners
        registrationPageDisplayer.attach(actionListener);
        subscription = registrationPageService.getRegistrationPassCheck()
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if (aBoolean)
                        {
                            //show verify email dialog
                            registrationPageDisplayer.showRegistrationAlertDialog(R.string.registration_dialog_text);
                        }
                    }
                });
    }

    //Stop showing / presenting
    public void stopPresenting() {
        registrationPageDisplayer.detach(actionListener);
        subscription.unsubscribe();
    }

}
