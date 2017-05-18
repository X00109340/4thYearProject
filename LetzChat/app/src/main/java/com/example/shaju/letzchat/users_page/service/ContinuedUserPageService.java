package com.example.shaju.letzchat.users_page.service;
import rx.Observable;

import com.example.shaju.letzchat.users_page.model.User;
import com.example.shaju.letzchat.users_page.model.Users;
import com.example.shaju.letzchat.users_page.database.UserDatabase;


/**
 * Created by Shajun on 12/02/2017.
 */


public class ContinuedUserPageService implements UserPageService {

    //Represents user DATABASE
    private final UserDatabase userDatabase;

    public ContinuedUserPageService(UserDatabase userDatabase) {
        this.userDatabase = userDatabase;
    }

    //Set the profile image of the user
    @Override
    public void setUserProfilePicture(User userIN, String imageIN) {
        userDatabase.setNewUserImage(userIN.getUid(), imageIN);
    }

    //Set the name of the users
    @Override
    public void setUserOnlineId(User userIN, String nameIN)
    {
        userDatabase.setNewOnlineId(userIN.getUid(), nameIN);
    }

    //Set the name of the users
    @Override
    public void setPassword(User userIN, String passwordIN)
    {
        userDatabase.setNewOnlineId(userIN.getUid(), passwordIN);
    }

    //get user with id
    @Override
    public Observable<User> getSpecificUser(String userId) {
        return userDatabase.observeSpecificUser(userId);
    }

    //get all users
    @Override
    public Observable<Users> getAllUsers() {
        return userDatabase.singleObserveUsers();
    }

    //sync users
    @Override
    public Observable<Users> syncUsers() {
        return userDatabase.observeAllUsers();
    }

}
