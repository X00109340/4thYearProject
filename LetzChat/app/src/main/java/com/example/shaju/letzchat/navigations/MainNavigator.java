package com.example.shaju.letzchat.navigations;

/**
 * Based on from https://github.com/novoda/bonfire-firebase-sample/blob/master/android/core/src/main/java/com/novoda/bonfire/navigation/LoginNavigator.java
 */

public interface MainNavigator {

    void toConversations();

    void toGlobalRoom();

    void toUserList();

    void toInvite();

    void toProfile();



    void init();

    void toGoogleSignOut(int method);

    void toFirstLogin();

    void toLogin();

    Boolean onBackPressed();

}
