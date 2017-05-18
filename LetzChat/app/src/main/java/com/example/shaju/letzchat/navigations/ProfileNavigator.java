package com.example.shaju.letzchat.navigations;

import com.example.shaju.letzchat.users_page.model.User;

import android.graphics.Bitmap;
import android.widget.TextView;

/**
 * Based on from https://github.com/novoda/bonfire-firebase-sample/blob/master/android/core/src/main/java/com/novoda/bonfire/navigation/LoginNavigator.java
 */

public interface ProfileNavigator extends Navigator {


    interface ProfileDialogListener {

        void onNameSelected(String textIN, User userIN);

        void onPasswordSelected(String textIN);


        void onImageSelected(Bitmap bitmapIN);

    }

    void showImagePicker();

    void showInputTextDialog(String hintIN, TextView textViewIN, User userIN);

    void showInputPasswordDialog(String hintIN, User userIN);

    void attach(ProfileDialogListener profileDialogListenerIN);

    void detach(ProfileDialogListener profileDialogListenerIN);


}
