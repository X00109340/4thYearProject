package com.example.shaju.letzchat.registration_page.service;

import rx.Observable;

/**
 * Created by Shajun on 24/02/2017.
 */

public interface RegistrationPageService {

    //Register with email and password - basic registration
    void registerUserEmailAndPassword(String emailIN, String passwordIN);

    Observable<Boolean> getRegistrationPassCheck();

}
