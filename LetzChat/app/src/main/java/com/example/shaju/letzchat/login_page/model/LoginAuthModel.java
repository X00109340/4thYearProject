package com.example.shaju.letzchat.login_page.model;

import com.example.shaju.letzchat.users_page.model.User;


public class LoginAuthModel {

    //Throw failure
    private final Throwable failure;
    //Represents a user
    private final User user;

    //Constructor passing in user
    public LoginAuthModel(User userIN) {
        user = userIN;
        this.failure = null;
    }

    //Constructor passing in throwable
    public LoginAuthModel(Throwable throwableFailureIN) {
        this.user = null;
        failure = throwableFailureIN;
    }


    //Return user
    public User getUser() {
        //if user is null
        if (user == null)
        {
            throw new IllegalStateException("LoginAuthModel Failed");
        }
        //else return user
        return user;
    }

    public Throwable getFailure() {
        if (failure == null) {
            throw new IllegalStateException("LoginAuthModel Successful");
        }
        return failure;
    }

    public boolean isSuccess() {
        return user != null;
    }




}
