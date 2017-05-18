package com.example.shaju.letzchat.global_chat.service;

import com.example.shaju.letzchat.global_chat.model.Chat;
import com.example.shaju.letzchat.global_chat.model.Message;
import com.example.shaju.letzchat.firebase_database.FirebaseDatabaseResult;

import rx.Observable;


public interface GlobalChatPageService {

    //Send a message to chat
    void sendMessage(Message message);

    //Event
    Observable<FirebaseDatabaseResult<Chat>> getChat();

    //Sync all messages
    Observable<Message> syncAllMessages();

}
