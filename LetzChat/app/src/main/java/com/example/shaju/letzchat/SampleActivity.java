package com.example.shaju.letzchat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.Digits;
import com.digits.sdk.android.DigitsAuthButton;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsSession;
import com.example.shaju.letzchat.first_time_login.FirstTimeUserLoginActivity;
import com.example.shaju.letzchat.private_conversations.ConversationActivity;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import io.fabric.sdk.android.Fabric;

public class SampleActivity extends AppCompatActivity {
    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "3TFXaSzBxPz2lHTIdIxmRK3Vh";
    private static final String TWITTER_SECRET = "U1Bazabg4txxtViOiTUWYNZfoUwHJeFarJi7keqGllQQ3D1Wc4";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new TwitterCore(authConfig), new Digits.Builder().build());
        setContentView(R.layout.activity_sample);

//        DigitsAuthButton digitsButton = (DigitsAuthButton) findViewById(R.id.auth_button);
//        digitsButton.setCallback(new AuthCallback() {
//            @Override
//            public void success(DigitsSession session, String phoneNumber) {
//                // TODO: associate the session userID with your user model
//                Toast.makeText(getApplicationContext(), "Authentication successful for "
//                        + phoneNumber, Toast.LENGTH_LONG).show();
//
//                Intent myIntent = new Intent(SampleActivity.this, ConversationActivity.class);
//                SampleActivity.this.startActivity(myIntent);
//                finish();
//
//            }
//
//            @Override
//            public void failure(DigitsException exception) {
//                Log.d("Digits", "Sign in with Digits failure", exception);
//            }
//        });

    }

}
