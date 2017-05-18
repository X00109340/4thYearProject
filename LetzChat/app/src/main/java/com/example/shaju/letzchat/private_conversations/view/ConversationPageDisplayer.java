package com.example.shaju.letzchat.private_conversations.view;

import com.example.shaju.letzchat.private_conversations.model.Message;

import com.example.shaju.letzchat.private_conversations.model.Chat;



public interface ConversationPageDisplayer {

    interface ConversationListener {

        //Message submit
        void onSubmitMessage(String message);

        //message length - Not necessary
        void onMessageLengthChanged(int messageLength);

        //Go to parent
        void onUpPressed();

    }

    //Show typing indicator
    void showTyping();

    //hide typing indicator
    void hideTyping();

    //To displayUserDetails all the chat for user
    void display(Chat chatIN, String userNameIN);

    //Add new message
    void addToDisplay(Message messagesIN, String usernameIN);

    //show the receiver photo, name, image
    void setupToolbar(String userdetailIN, String imageIN, long lastSeenTimeIN);


    void attach(ConversationListener conversationInteractionListener);

    void detach(ConversationListener conversationInteractionListener);


    void enableInteraction();

    void disableInteraction();



}
