package com.example.shaju.letzchat.profile_page;

import com.example.shaju.letzchat.ChatDependencies;
import com.example.shaju.letzchat.navigations.AndroidProfileNavigator;
import com.example.shaju.letzchat.profile_page.presenter.ProfilePagePresenter;
import com.example.shaju.letzchat.profile_page.view.ProfilePageDisplayer;
import com.example.shaju.letzchat.BaseActivity;
import com.example.shaju.letzchat.R;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;


/**
 * Created by Shajun on 24/03/2017.
 */

public class ProfilePageActivity extends BaseActivity {
    //Profile navigator from navigations folder
    private AndroidProfileNavigator androidProfileNavigator;

    //Present the profile
    private ProfilePagePresenter profilePagePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set the profile activity XML file
        setContentView(R.layout.profile_activity);
        //set the main_toolbar
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        //Toolbar title
        getSupportActionBar().setTitle("Profile Settings");
        //set the profile view
        ProfilePageDisplayer profilePageDisplayer = (ProfilePageDisplayer) findViewById(R.id.profileView);

        androidProfileNavigator = new AndroidProfileNavigator(this);

        profilePagePresenter = new ProfilePagePresenter(ChatDependencies.INSTANCE.getLoginService(), ChatDependencies.INSTANCE.getUserService(), ChatDependencies.INSTANCE.getProfileService(),
                ChatDependencies.INSTANCE.getStorageService(), profilePageDisplayer, androidProfileNavigator
        );
    }



    //Start profile presenting
    @Override
    protected void onStart() {
        super.onStart();
        profilePagePresenter.startPresenting();
    }


    //Stop presenting
    @Override
    protected void onStop() {
        super.onStop();
        profilePagePresenter.stopPresenting();
    }

    //Destroy or stop presenting
    @Override
    protected void onDestroy() {
        super.onDestroy();
        profilePagePresenter.stopPresenting();
    }

    @Override
    protected void onActivityResult(int requestCodeIN, int resultCodeIN, Intent intentIN)
    {
        if (!androidProfileNavigator.onActivityResult(requestCodeIN, resultCodeIN, intentIN))
        {
            super.onActivityResult(requestCodeIN, resultCodeIN, intentIN);
        }
    }

}