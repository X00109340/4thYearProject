package com.example.shaju.letzchat.main_page.database;

import com.example.shaju.letzchat.users_page.model.User;

import rx.Observable;


//For cloud messages - push notifications
public interface CloudMessagingTokenDatabase {

    //Enable token
    void enableUserToken(String userIdIN);

    //Disable token
    void disableUserToken(String userIdIN);


    //Set token for user
    void setUserToken(User user);

    //To read token of user
    Observable<String> readUserToken(User user);




}
