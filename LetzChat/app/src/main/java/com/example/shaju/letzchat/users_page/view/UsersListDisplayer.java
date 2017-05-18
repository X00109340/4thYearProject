package com.example.shaju.letzchat.users_page.view;

import com.example.shaju.letzchat.users_page.model.User;
import com.example.shaju.letzchat.users_page.model.Users;

/**
 * Created by Shajun on 12/02/2017.
 */

public interface UsersListDisplayer {

    interface OnUserClickInteractionListener {

        void onSpecificUserSelection(User user);

    }

    //Display users
    void displayAllUsers(Users usersIN);

    //Filter user based on id searched
    void filteredSearch(String idIN);

    //Attach and Detach listeners
    void attach(OnUserClickInteractionListener onUserClickInteractionListenerIN);
    void detach(OnUserClickInteractionListener onUserClickInteractionListenerIN);



}
