package com.example.shaju.letzchat.registration_page;

import com.example.shaju.letzchat.registration_page.presenter.RegistrationPagePresenter;
import com.example.shaju.letzchat.navigations.AndroidRegistrationNavigator;
import com.example.shaju.letzchat.BaseActivity;
import com.example.shaju.letzchat.ChatDependencies;
import com.example.shaju.letzchat.R;
import com.example.shaju.letzchat.registration_page.view.RegistrationPageDisplayer;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;

/**
 * Created by Shajun on 27/02/2017.
 */

public class RegistrationPageActivity extends BaseActivity {
    //Registration androidRegistrationNavigator
    private AndroidRegistrationNavigator androidRegistrationNavigator;

    //Presenter
    private RegistrationPagePresenter registrationPagePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set the activity
        setContentView(R.layout.userregistration_activity);
        //set the main_toolbar - empty main_toolbar (no title)
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        RegistrationPageDisplayer registrationPageDisplayer = (RegistrationPageDisplayer) findViewById(R.id.registrationView);

        androidRegistrationNavigator = new AndroidRegistrationNavigator(this);
        registrationPagePresenter = new RegistrationPagePresenter(ChatDependencies.INSTANCE.getRegistrationService(), registrationPageDisplayer, androidRegistrationNavigator);
    }

    //On start
    @Override
    protected void onStart() {
        super.onStart();
        registrationPagePresenter.startPresenting();
    }

    //on stop
    @Override
    protected void onStop() {
        super.onStop();
        registrationPagePresenter.stopPresenting();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        registrationPagePresenter.stopPresenting();
    }

    @Override
    protected void onActivityResult(int requestCodeIN, int resultCodeIN, Intent intentIN)
    {
        super.onActivityResult(requestCodeIN, resultCodeIN, intentIN);
    }



}
