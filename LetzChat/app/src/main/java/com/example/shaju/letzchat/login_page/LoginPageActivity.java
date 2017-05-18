package com.example.shaju.letzchat.login_page;



import com.example.shaju.letzchat.BaseActivity;
import com.example.shaju.letzchat.ChatDependencies;
import com.example.shaju.letzchat.R;
import com.example.shaju.letzchat.login_page.presenter.LoginPagePresenter;
import com.example.shaju.letzchat.login_page.view.LoginPageDisplayer;
import com.example.shaju.letzchat.navigations.AndroidLoginNavigator;
import com.example.shaju.letzchat.navigations.AndroidNavigator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

/**
 * Created by Shajun on 15/03/2017.
 */

public class LoginPageActivity extends BaseActivity {

    //Android login navigator
    private AndroidLoginNavigator androidLoginNavigator;

    //Login loginPagePresenter
    private LoginPagePresenter loginPagePresenter;


    //on Create
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set the activity
        setContentView(R.layout.login_activity);
        //set the main_toolbar
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        //Login displayUserDetails
        LoginPageDisplayer loginPageDisplayer = (LoginPageDisplayer) findViewById(R.id.loginView);
        //Login with Google
        LoginUsingGoogleApiClient loginUsingGoogleApiClient = new LoginUsingGoogleApiClient(this);
        loginUsingGoogleApiClient.setupGoogleApiClient();
        //
        androidLoginNavigator = new AndroidLoginNavigator(this, loginUsingGoogleApiClient, new AndroidNavigator(this));
        loginPagePresenter = new LoginPagePresenter(ChatDependencies.INSTANCE.getLoginService(), loginPageDisplayer, androidLoginNavigator);
    }



    //ON START
    @Override
    protected void onStart() {
        super.onStart();
        loginPagePresenter.startPresenting();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    //On STop
    @Override
    protected void onStop() {
        super.onStop();
        loginPagePresenter.stopPresenting();
    }

    @Override
    protected void onActivityResult(int requestCodeIN, int resultCodeIN, Intent intentIN) {
        if (!androidLoginNavigator.onActivityResult(requestCodeIN, resultCodeIN, intentIN))
        {
            super.onActivityResult(requestCodeIN, resultCodeIN, intentIN);
        }
    }
}

