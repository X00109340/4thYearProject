package com.example.shaju.letzchat.main_page.service;

import rx.Observable;

import com.example.shaju.letzchat.main_page.database.CloudMessagingTokenDatabase;
import com.example.shaju.letzchat.users_page.model.User;


/**
 * Created by Shajun on 16/03/2017.
 */

public class FirebaseCloudMessagingTokenService implements CloudMessagingTokenService {

    //Interface cloud messaging service
    private CloudMessagingTokenDatabase cloudMessagingTokenDatabase;

    //constructor
    public FirebaseCloudMessagingTokenService(CloudMessagingTokenDatabase cloudMessagingTokenDatabaseIN)
    {
        cloudMessagingTokenDatabase = cloudMessagingTokenDatabaseIN;
    }

    //Set token for user
    @Override
    public void setUserToken(User user) {
        cloudMessagingTokenDatabase.setUserToken(user);
    }

    //Read user token
    @Override
    public Observable<String> readUserToken(User userIN)
    {
        return cloudMessagingTokenDatabase.readUserToken(userIN);
    }
}
