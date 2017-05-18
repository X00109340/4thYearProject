package com.example.shaju.letzchat.global_chat.model;

import com.example.shaju.letzchat.users_page.model.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Comparator;
import java.util.HashMap;

public class Chat {

    //Users map
    private HashMap<String,User> userHashMap;

    //Message list
    private final List<Message> usersMessageList;

    //Constructor
    public Chat(List<Message> messageListIN) {
        this.usersMessageList = messageListIN;
        this.userHashMap = new HashMap<>();
    }
    //Add a new message to the list if that message is not in the list
    public void addMessage(Message messageIN) {
        if (!usersMessageList.contains(messageIN)) {
            usersMessageList.add(messageIN);
        }
    }

    //Return the length of the list
    public int size() {
        return usersMessageList.size();
    }

    //Return all the messages
    public List<Message> getMessages()
    {
        return usersMessageList;
    }

    //Get message at position
    public Message get(int positionIN)
    {
        return usersMessageList.get(positionIN);
    }

    //Add user to map
    public void addUser(User user)
    {
        userHashMap.put(user.getUid(),user);
    }

    //get user from map
    public User getUser(String uidIN)
    {
        return userHashMap.get(uidIN);
    }

    //To sort the message by date
    private static Comparator<? super Message> byDate() {
        return new Comparator<Message>() {
            @Override
            public int compare(Message o1, Message o2) {
                long time1 = Long.parseLong(o1.getTimestamp().replace("/",""));
                long time2 = Long.parseLong(o2.getTimestamp().replace("/",""));
                return time1 < time2 ? -1: time1 > time2 ? 1: 0;
            }
        };
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Chat chat = (Chat) o;

        return usersMessageList != null ? usersMessageList.equals(chat.usersMessageList) : chat.usersMessageList == null;

    }

    //Sort by date
    public Chat sortedByDate() {
        //new messages list
        List<Message> sortedList = new ArrayList<>(usersMessageList);
        //sort messages by date
        Collections.sort(sortedList,byDate());
        return new Chat(sortedList);
    }




    @Override
    public int hashCode() {
        return usersMessageList != null ? usersMessageList.hashCode() : 0;
    }

}
