package com.example.shaju.letzchat.global_chat.database;
import com.example.shaju.letzchat.global_chat.model.Message;
import rx.Observable;
import com.example.shaju.letzchat.global_chat.model.Chat;



public interface GlobalChatPageDatabase {

    //Send a new message
    void sendMessage(Message message);

    //listen for new add message events
    Observable<Message> observeAddMessage();

    //Listen for new chats - not used
    Observable<Chat> observeChat();


}

