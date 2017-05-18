package com.example.shaju.letzchat.navigations;

import com.example.shaju.letzchat.login_page.LoginPageActivity;
import com.example.shaju.letzchat.main_page.MainPageActivity;

import android.app.Activity;
import android.content.Intent;


/**
 * Created by Shajun on 17/03/2017.
 */

public class AndroidNavigator implements Navigator {

    //Activity to go to
    private final Activity activity;

    //Constructor
    public AndroidNavigator(Activity activity) {
        this.activity = activity;
    }

    //Parent activity
    @Override
    public void toParent() {
        activity.finish();
    }


    //Go to the main activity
    @Override
    public void toMainActivity() {
        activity.startActivity(new Intent(activity, MainPageActivity.class));
        activity.finish();

    }


    //Go to login activity
    @Override
    public void toLogin() {
        Intent intent = new Intent(activity, LoginPageActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.finish();
    }




}
