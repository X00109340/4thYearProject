package com.example.shaju.letzchat.global_chat.service;

import com.example.shaju.letzchat.firebase_database.FirebaseDatabaseResult;
import com.example.shaju.letzchat.global_chat.model.Chat;
import com.example.shaju.letzchat.global_chat.model.Message;
import com.example.shaju.letzchat.global_chat.database.GlobalChatPageDatabase;


import rx.Observable;
import rx.functions.Func1;



public class ContinuedGlobalChatPageService implements GlobalChatPageService
{

    //Global chat database class reference
    private final GlobalChatPageDatabase globalChatPageDatabase;

    //constructor
    public ContinuedGlobalChatPageService(GlobalChatPageDatabase globalChatPageDatabaseIN)
    {
        this.globalChatPageDatabase = globalChatPageDatabaseIN;
    }

    //Send a new message
    @Override
    public void sendMessage(Message messageIN)
    {
        globalChatPageDatabase.sendMessage(messageIN);
    }

    //Listen to events - get chat
    @Override
    public Observable<FirebaseDatabaseResult<Chat>> getChat()
    {
        return globalChatPageDatabase.observeChat()
                .map(asDatabaseResult())
                .onErrorReturn(FirebaseDatabaseResult.<Chat>errorAsDatabaseResult());
    }

    //Listen to new messages by synchronisation
    @Override
    public Observable<Message> syncAllMessages()
    {
        return globalChatPageDatabase.observeAddMessage();
    }


    //Not Used
    private static Func1<Chat, FirebaseDatabaseResult<Chat>> asDatabaseResult()
    {
        return new Func1<Chat, FirebaseDatabaseResult<Chat>>() {
            @Override
            public FirebaseDatabaseResult<Chat> call(Chat chat) {
                return new FirebaseDatabaseResult<>(chat);
            }
        };
    }


}
