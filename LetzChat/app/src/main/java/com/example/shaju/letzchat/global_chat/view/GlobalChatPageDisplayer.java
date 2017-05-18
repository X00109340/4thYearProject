package com.example.shaju.letzchat.global_chat.view;

import com.example.shaju.letzchat.global_chat.model.Chat;
import com.example.shaju.letzchat.global_chat.model.Message;
import com.example.shaju.letzchat.users_page.model.User;
import com.example.shaju.letzchat.users_page.model.Users;



public interface GlobalChatPageDisplayer {

    //interface
    interface GlobalChatPageActionListener {

        void onMessageLengthChanged(int messageLengthIN);

        //on message submit
        void onMessageSend(String messageIN);

    }

    //Display all the messages
    void display(Chat chatIN, Users usersIN, User userIN);

    //Add messages to the displayUserDetails
    void addToDisplay(Message messageIN, User senderIN, User userIN);

    //Enable or disable interactions
    void enableInteraction();
    void disableInteraction();

    //Listeners
    void attach(GlobalChatPageActionListener globalChatPageActionListenerIN);
    void detach(GlobalChatPageActionListener globalChatPageActionListenerIN);


}
