package com.example.shaju.letzchat.main_page.service;

import com.example.shaju.letzchat.users_page.model.User;

import rx.Observable;



public interface CloudMessagingTokenService {

    //Set token for user
    void setUserToken(User user);

    //read token- event listener
    Observable<String> readUserToken(User userIN);

}
