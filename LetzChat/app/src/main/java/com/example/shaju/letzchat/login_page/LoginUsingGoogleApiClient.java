package com.example.shaju.letzchat.login_page;


import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import com.example.shaju.letzchat.R;
import android.support.v7.app.AppCompatActivity;

/**
 * This class is mostly based on
 * https://github.com/novoda/bonfire-firebase-sample/blob/master/android/app/src/main/java/com/novoda/bonfire/login/LoginGoogleApiClient.java
 */

public class LoginUsingGoogleApiClient {
    //Google api client
    private GoogleApiClient apiClient;
    //App Compact Activity
    private final AppCompatActivity activity;


    public LoginUsingGoogleApiClient(AppCompatActivity appCompatActivityIN) {
        activity = appCompatActivityIN;
    }

    public void setupGoogleApiClient() {
        String string = activity.getString(R.string.default_web_client_id);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(string)
                .requestEmail()
                .build();
        apiClient = new GoogleApiClient.Builder(activity)
                .enableAutoManage(
                        activity, new GoogleApiClient.OnConnectionFailedListener() {
                            @Override
                            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                                Log.d("LetzChat", "Failed to connect to GMS");
                            }
                        })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }


    public GoogleSignInResult getSignInResultFromIntent(Intent intentIN) {
        return Auth.GoogleSignInApi.getSignInResultFromIntent(intentIN);
    }


    public Intent getSignInIntent() {
        return Auth.GoogleSignInApi.getSignInIntent(apiClient);
    }


}

