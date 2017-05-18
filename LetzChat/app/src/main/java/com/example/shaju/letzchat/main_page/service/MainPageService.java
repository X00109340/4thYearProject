package com.example.shaju.letzchat.main_page.service;

import com.example.shaju.letzchat.users_page.model.User;



public interface MainPageService {


    //Last seen
    void initialiseLastSeenDisplay(User userIN);

    //
    String getLoginProvider() throws Exception;

    //Logout of application
    void applicationLogout();

}
