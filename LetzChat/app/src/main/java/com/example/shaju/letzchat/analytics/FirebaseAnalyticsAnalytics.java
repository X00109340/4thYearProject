package com.example.shaju.letzchat.analytics;

import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

//This class was called FirebaseAnalyticsAnalytics instead of FirebaseAnalytics because I'm importing a classs caleed FirebaseAnalytics

public class FirebaseAnalyticsAnalytics implements Analytics {

    //Remove account event
    private static final String REMOVE_ACCOUNT = "remove_account";

    //User ID event
    private static final String USER_ID = "user_id";


    private final FirebaseAnalytics firebaseAnalytics;


    @Override
    public void trackDeleteAccount(String userId) {
        Bundle bundle = new Bundle();
        bundle.putString(USER_ID, userId);
        firebaseAnalytics.logEvent(REMOVE_ACCOUNT, bundle);
    }

    public FirebaseAnalyticsAnalytics(FirebaseAnalytics firebaseAnalytics) {
        this.firebaseAnalytics = firebaseAnalytics;
    }

}
