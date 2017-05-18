package com.example.shaju.letzchat.private_conversations.service;

import rx.Observable;

import com.example.shaju.letzchat.private_conversations.model.Chat;
import com.example.shaju.letzchat.private_conversations.model.Message;
import com.example.shaju.letzchat.firebase_database.FirebaseDatabaseResult;



/**
 * Created by Shajun on 11/03/2017
 */

public interface ConversationPageService {

    //getTyping indicator on receiver side
    Observable<Boolean> getTyping(String sender, String receiver);

    //setTyping to true or false based on if user is typing or not
    void setTyping(String sender, String receiver, Boolean value);

    //Send button click
    void sendNewMessage(String user, Message message);

    //Sync all the previous messages
    Observable<Message> syncMessages(String sender, String receiver);

    //
    Observable<FirebaseDatabaseResult<Chat>> getChat(String sender, String receiver);





}
