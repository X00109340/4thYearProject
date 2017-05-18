package com.example.shaju.letzchat.main_page;


import com.example.shaju.letzchat.ChatDependencies;
import com.example.shaju.letzchat.main_page.view.MainPageDisplayer;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.example.shaju.letzchat.R;
import com.example.shaju.letzchat.main_page.presenter.MainPresenter;
import com.example.shaju.letzchat.BaseActivity;
import com.example.shaju.letzchat.navigations.AndroidMainNavigator;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.content.Intent;
import android.support.v7.widget.Toolbar;

/**
 * Created by Shajun on 17/03/2017.
 */

public class MainPageActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener {

    //Main navigator
    private AndroidMainNavigator androidMainNavigator;

    //Main mainPresenter
    private MainPresenter mainPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Set the activity
        setContentView(R.layout.main_drawer_activity);
        MainPageDisplayer mainPageDisplayer = (MainPageDisplayer) findViewById(R.id.mainView);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();

        //Android navigator initialised
        androidMainNavigator = new AndroidMainNavigator(this, googleApiClient);

        //main presenter initialised
        mainPresenter = new MainPresenter(ChatDependencies.INSTANCE.getLoginService(), ChatDependencies.INSTANCE.getUserService(), mainPageDisplayer, ChatDependencies.INSTANCE.getMainService(),
                ChatDependencies.INSTANCE.getMessagingService(), androidMainNavigator, ChatDependencies.INSTANCE.getFirebaseToken(), this);
    }

    //back button pressed
    @Override
    public void onBackPressed() {
        if (!mainPresenter.onBackPressed())
            if (!androidMainNavigator.onBackPressed())
            {
                super.onBackPressed();
            }
    }


    @Override
    protected void onActivityResult(int requestCodeIN, int resultCodeIN, Intent intentIN) {
        if (!androidMainNavigator.onActivityResult(requestCodeIN, resultCodeIN, intentIN))
        {
            super.onActivityResult(requestCodeIN, resultCodeIN, intentIN);
        }
    }

    //On start up
    @Override
    protected void onStart() {
        super.onStart();
        mainPresenter.startPresenting();
    }

    //On application close or stop
    @Override
    protected void onStop() {
        super.onStop();
        mainPresenter.stopPresenting();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

}

