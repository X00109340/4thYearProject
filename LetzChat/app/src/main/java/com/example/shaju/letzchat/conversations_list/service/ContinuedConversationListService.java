package com.example.shaju.letzchat.conversations_list.service;

import rx.Observable;

import com.example.shaju.letzchat.private_conversations.database.ConversationPageDatabase;
import com.example.shaju.letzchat.private_conversations.model.Message;
import com.example.shaju.letzchat.conversations_list.database.ConversationListDb;
import com.example.shaju.letzchat.users_page.database.UserDatabase;
import com.example.shaju.letzchat.users_page.model.User;

import java.util.List;



//Parts are From github:
public class ContinuedConversationListService implements  ConversationListService {

    //ConversationListDb
    private final ConversationListDb conversationListDb;
    //ConversationPageDatabase
    private final ConversationPageDatabase conversationPageDatabase;
    //UserDatabase
    private final UserDatabase userDatabase;

    public ContinuedConversationListService(ConversationListDb conversationListDbIN, ConversationPageDatabase conversationPageDatabaseIN, UserDatabase userDatabaseIN) {
        userDatabase = userDatabaseIN;
        conversationListDb = conversationListDbIN;
        conversationPageDatabase = conversationPageDatabaseIN;
    }

    //Represents chat for user logged in
    @Override
    public Observable<List<String>> getConversationsFor(User user) {
        return conversationListDb.observeConversationsFor(user);
    }

    //Retrun the last message for each chat to show as a preview for the overall chat list
    @Override
    public Observable<Message> getLastMessageFor(User self, User destination) {
        return conversationPageDatabase.observableLastMessage(self.getUid(),destination.getUid());
    }


}
