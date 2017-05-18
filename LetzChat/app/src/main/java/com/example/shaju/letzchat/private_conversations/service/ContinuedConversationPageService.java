package com.example.shaju.letzchat.private_conversations.service;

import rx.Observable;
import rx.functions.Func1;

import com.example.shaju.letzchat.firebase_database.FirebaseDatabaseResult;
import com.example.shaju.letzchat.private_conversations.model.Chat;
import com.example.shaju.letzchat.private_conversations.model.Message;
import com.example.shaju.letzchat.private_conversations.database.ConversationPageDatabase;


/**
 * Created by Shajun on 11/03/2017
 */

public class ContinuedConversationPageService implements ConversationPageService {

    //ConversationModel database
    private final ConversationPageDatabase conversationPageDatabase;

    //Constructor pass in database
    public ContinuedConversationPageService(ConversationPageDatabase conversationPageDatabase)
    {
        //initialise the database
        this.conversationPageDatabase = conversationPageDatabase;
    }

    //Sync all the messages that are in the database previously
    @Override
    public Observable<Message> syncMessages(String sender, String receiver) {
        return conversationPageDatabase.observableAddMessage(sender, receiver);
    }

    @Override
    public Observable<FirebaseDatabaseResult<Chat>> getChat(String sender, String receiver) {
        return conversationPageDatabase.observableChat(sender, receiver)
                .map(asDatabaseResult())
                .onErrorReturn(FirebaseDatabaseResult.<Chat>errorAsDatabaseResult());
    }

    private static Func1<Chat, FirebaseDatabaseResult<Chat>> asDatabaseResult() {
        return new Func1<Chat, FirebaseDatabaseResult<Chat>>() {
            @Override
            public FirebaseDatabaseResult<Chat> call(Chat chat) {
                return new FirebaseDatabaseResult<>(chat);
            }
        };
    }

    //
    @Override
    public void sendNewMessage(String self, Message message) {
        conversationPageDatabase.sendMessage(self, message);
    }

    //get typing indicator for receiver
    @Override
    public Observable<Boolean> getTyping(String sender, String receiver) {
        return conversationPageDatabase.observableTyping(sender, receiver);
    }

    //set typing indicator user
    //Value - true / false
    @Override
    public void setTyping(String sender, String receiver, Boolean value) {
        conversationPageDatabase.setTyping(sender, receiver,value);
    }
}
