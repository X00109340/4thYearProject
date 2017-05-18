package com.example.shaju.letzchat.users_page.model;

import java.util.Comparator;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shajun on 10/02/2017.
 */

public class Users {

    //We have a list of userList
    private final List<User> userList;

    //Constructor for
    public Users(List<User> userList)
    {
        this.userList = userList;
    }

    //Remove the user
    public void remove(User userIN)
    {
        if (userList.contains(userIN))
        {
            userList.remove(userIN);
        }
    }


    //Sort users according to name
    public Users sortedByName() {
        //Create a new list
        List<User> sortedList = new ArrayList<>(userList);
        //sort it
        Collections.sort(sortedList,byName());
        //return
        return new Users(sortedList);
    }

    private static Comparator<? super User> byName() {
        return new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                return o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase());
            }
        };
    }

    //Return all the users in list
    public List<User> getUsers()
    {
        return userList;
    }

    //Return the size of the list
    public int size()
    {
        return userList.size() ;
    }

    //Return details of user at position _____
    public User getUserAt(int positionIN)
    {
        return userList.get(positionIN);
    }

    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Users users1 = (Users) o;

        return userList.equals(users1.userList);

    }

}