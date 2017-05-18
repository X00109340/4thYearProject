package com.example.shaju.letzchat.private_conversations.database;

import rx.Observable;

import com.example.shaju.letzchat.private_conversations.model.Chat;
import com.example.shaju.letzchat.private_conversations.model.Message;

//Observable's notifyObservers method causes all of its observers to be notified of the change by a call to their update method.
public interface ConversationPageDatabase {

    //Notify of new message
    Observable<Message> observableAddMessage(String sender, String destination);

    //Last message send
    Observable<Message> observableLastMessage(String sender, String destination);

    //Overall chat observe
    Observable<Chat> observableChat(String sender, String destination);

    //To observe if the user is typing anything
    Observable<Boolean> observableTyping(String sender, String destination);

    //Is Typing indicator for users when the user is typing
    void setTyping(String sender, String destination, Boolean value);

    //Notify of message being send
    void sendMessage(String user, Message message);


}
