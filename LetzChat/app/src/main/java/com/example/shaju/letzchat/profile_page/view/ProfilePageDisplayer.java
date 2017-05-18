package com.example.shaju.letzchat.profile_page.view;
import com.example.shaju.letzchat.users_page.model.User;

import android.graphics.Bitmap;
import android.widget.TextView;


/**
 * Created by Shajun on 24/03/2017.
 */


public interface ProfilePageDisplayer {

    interface ProfileOnClickActionListeners {

        void onImagePressed();

        void onOnlineIdPressed(String hintIN, TextView textViewIN);

        void onChangePasswordPressed(String hintIN);

        void onUpPressed();
    }

    //Attach listeners
    void attach(ProfileOnClickActionListeners profileOnClickActionListenersIN);
    void detach(ProfileOnClickActionListeners profileOnClickActionListenersIN);

    //Pass in user details to displayUserDetails
    void displayUserDetails(User userDetailsIN);

    //update image
    void updateUserCurrentProfileImage(Bitmap imageBitmapIM);



}
