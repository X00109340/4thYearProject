package com.example.shaju.letzchat.users_page.service;
import rx.Observable;

import com.example.shaju.letzchat.users_page.model.User;
import com.example.shaju.letzchat.users_page.model.Users;

/**
 * Created by Shajun on 12/02/2017.
 */

public interface UserPageService {

    //Profile picture of each user
    void setUserProfilePicture(User userIN, String imageIN);

    //Name of each user (Online ID)
    void setUserOnlineId(User userIN, String nameIN);

    //set new password
    void setPassword(User userIN, String passwordIN);

    //Get user with specific USER ID
    Observable<User> getSpecificUser(String userId);

    //Return all users
    Observable<Users> getAllUsers();

    //Synchronise the users
    Observable<Users> syncUsers();



}