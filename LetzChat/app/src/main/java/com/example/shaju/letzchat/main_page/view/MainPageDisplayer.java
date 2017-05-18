package com.example.shaju.letzchat.main_page.view;

import android.view.MenuItem;

import com.example.shaju.letzchat.users_page.model.User;

/**
 * Created by Shajun on 16/03/2017.
 */

public interface MainPageDisplayer {


    void inflateMenu();

    void clearMenu();

    //Set title
    void setTitle(String title);

    //set user (name and email and prfile pic)
    void setUserDetails(User user);

    //when back button is pressed
    boolean onBackPressed();

    //open drawer on button press
    void openDrawer();

    //Close drawer when clicked on a on actionlistner or clicked back
    void closeDrawer();

    void attach(DrawerClickActionListeners drawerClickActionListenersIN, NavigationActionListener navigationActionListenerIN, SearchUserActionListener searchUserActionListenerIN);

    void detach(DrawerClickActionListeners drawerClickActionListenersIN, NavigationActionListener navigationActionListenerIN, SearchUserActionListener searchUserActionListenerIN);

    interface NavigationActionListener {

        //Open navigation drawer
        void onHamburgerPressed();

    }

    interface DrawerClickActionListeners {

        //when navigation items are selected
        void navigationMenuItemSelected(MenuItem menuItemIN);

        //When the header is selected - go to profile activity
        void headerProfileSelected();


    }


    //Search for user
    interface SearchUserActionListener {

        void displayFilteredUserList(String textIN);

    }

}
