package com.example.shaju.letzchat.navigations;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by Shajun on 17/03/2017.
 */

public class AndroidRegistrationNavigator implements RegistrationNavigator {

    private final AppCompatActivity appCompatActivity;

    public AndroidRegistrationNavigator(AppCompatActivity appCompatActivityIN)
    {
        appCompatActivity = appCompatActivityIN;
    }

    @Override
    public void toLogin() {
        appCompatActivity.finish();
    }

    @Override
    public void toMainActivity() {

        //NOT USED BUT INHERITED
    }

    @Override
    public void toParent() {
        //NOT USED BUT INHERITED
    }


}
