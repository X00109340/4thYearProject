package com.example.shaju.letzchat.main_page.service;

import rx.Subscriber;

import com.example.shaju.letzchat.main_page.database.CloudMessagingTokenDatabase;
import com.example.shaju.letzchat.users_page.model.User;
import com.example.shaju.letzchat.users_page.database.UserDatabase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Shajun on 16/03/2017.
 */

public class ContinuedMainPageService implements MainPageService {

    //CLoud messaging database class
    private final CloudMessagingTokenDatabase cloudMessagingTokenDatabase;
    //User database class
    private final UserDatabase userDatabase;
    //Firebase authentication
    private final FirebaseAuth firebaseAuth;


    //Constructor
    public ContinuedMainPageService(FirebaseAuth firebaseAuthIN, UserDatabase userDatabaseIN, CloudMessagingTokenDatabase cloudMessagingTokenDatabaseIN) {
        firebaseAuth = firebaseAuthIN;
        cloudMessagingTokenDatabase = cloudMessagingTokenDatabaseIN;
        userDatabase = userDatabaseIN;
    }



    @Override
    public void applicationLogout() {
        //disable token so the user will not receive notifications
        cloudMessagingTokenDatabase.disableUserToken(firebaseAuth.getCurrentUser().getUid());
        //authentication sign out
        firebaseAuth.signOut();
    }

    @Override
    public String getLoginProvider() throws Exception {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            return firebaseUser.getProviders().get(0);
        }
        else {
            throw new Exception("Provider could not be reached");
        }
    }

    //Initialise last seen for user
    @Override
    public void initialiseLastSeenDisplay(final User userIN) {
        userDatabase.initialiseUserLastOnline()
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Boolean result) {
                        if (result.equals(Boolean.TRUE)) {
                            userDatabase.setUserLastOnline(userIN.getUid());
                        }
                    }
                });
    }

}
