package com.example.shaju.letzchat.navigations;

import com.example.shaju.letzchat.conversations_list.ConversationsListActivity;
import com.example.shaju.letzchat.first_time_login.FirstTimeUserLoginActivity;
import com.example.shaju.letzchat.users_page.UsersPageActivity;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.android.gms.auth.api.Auth;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.example.shaju.letzchat.ChatDependencies;
import com.example.shaju.letzchat.global_chat.GlobalChatPageActivity;
import com.example.shaju.letzchat.login_page.LoginPageActivity;
import com.google.android.gms.common.api.GoogleApiClient;
import com.example.shaju.letzchat.profile_page.ProfilePageActivity;
import com.example.shaju.letzchat.R;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Shajun on 17/03/2017.
 */

public class AndroidMainNavigator implements MainNavigator {

    public static final int LOGOUT_GOOGLE = 1;

    private boolean doubleBackToExitPressedOnce = false;

    private boolean firstOpen = true;

    private final AppCompatActivity activity;
    private final GoogleApiClient googleApiClient;

    private static final String TAG = AndroidMainNavigator.class.getSimpleName();

    //Constructor
    public AndroidMainNavigator(AppCompatActivity activityIN, @Nullable GoogleApiClient googleApiClientIN) {
        activity = activityIN;
        googleApiClient = googleApiClientIN;
    }



    //Show all the conversations
    @Override
    public void toConversations() {
        ConversationsListActivity conversationsListActivity = new ConversationsListActivity();
        activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, conversationsListActivity)
                .commit();
    }

    //To global chat room
    @Override
    public void toGlobalRoom() {
        GlobalChatPageActivity globalChatPageActivity = new GlobalChatPageActivity();
        activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, globalChatPageActivity)
                .commit();
    }

    //Show all the users
    @Override
    public void toUserList() {
        UsersPageActivity usersPageActivity = new UsersPageActivity();
        activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, usersPageActivity)
                .commit();
    }

    //To invite another person
    @Override
    public void toInvite() {
        Intent intent = new AppInviteInvitation.IntentBuilder(activity.getString(R.string.main_invite_title))
                .setMessage(activity.getString(R.string.main_invite_message))
                .setCallToActionText(activity.getString(R.string.main_invite_cta))
                .build();
        activity.startActivityForResult(intent, 1);
    }

    @Override
    public void toProfile()
    {
        activity.startActivity(new Intent(activity,ProfilePageActivity.class));
    }


    @Override
    public void init()
    {
        if (firstOpen)
        {
            this.toConversations();
            firstOpen = false;
        }
    }



    @Override
    public void toGoogleSignOut(int method) {
        if (method == LOGOUT_GOOGLE)
        {
            Toast.makeText(activity,"You have been logged out",Toast.LENGTH_LONG).show();

            Auth.GoogleSignInApi.signOut(googleApiClient);
        }
    }

    //Go to login activity
    @Override
    public void toLogin() {
        ChatDependencies.INSTANCE.clearDependecies();
        activity.startActivity(new Intent(activity,LoginPageActivity.class));
    }

    //Go to Firstloginactivity
    @Override
    public void toFirstLogin() {
        activity.startActivity(new Intent(activity,FirstTimeUserLoginActivity.class));
    }

    @Override
    public Boolean onBackPressed() {
        if (doubleBackToExitPressedOnce)
            activity.finishAffinity();

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(activity, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run()
            {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
        return true;
    }

    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {

                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.VALUE, "inv_sent");

                String[] invitationIds = AppInviteInvitation.getInvitationIds(resultCode, data);
                Log.d(TAG, "Invitations sent: " + invitationIds.length);
            }
            else
            {
                Bundle payload = new Bundle();
                payload.putString(FirebaseAnalytics.Param.VALUE, "inv_not_sent");

            }
            return true;
        } else
        {
            return false;
        }
    }
}
