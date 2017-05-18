package com.example.shaju.letzchat.users_page.database;

import rx.Observable;

import com.example.shaju.letzchat.users_page.model.User;
import com.example.shaju.letzchat.users_page.model.Users;


/**
 * Created by Shajun on 10/02/2017.
 */

public interface UserDatabase {

    void setNewUserImage(String userIdIN, String imageIN);

    void setNewOnlineId(String userIdIN, String nameIN);

    void setUserPassword(String userIDIN, String passwordIN);

    void setUserLastOnline(String userIdIN);

    Observable<Users> observeAllUsers();

    Observable<Users> singleObserveUsers();

    Observable<User> observeSpecificUser(String userId);

    Observable<User> readUserFrom(String userId);

    Observable<Boolean> initialiseUserLastOnline();


}
