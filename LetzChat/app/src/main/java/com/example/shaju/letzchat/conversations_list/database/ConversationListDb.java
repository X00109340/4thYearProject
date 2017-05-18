package com.example.shaju.letzchat.conversations_list.database;

import rx.Observable;
import com.example.shaju.letzchat.users_page.model.User;

import java.util.List;



/**
 * Created by Shajun on 11/03/2017.
 */

public interface ConversationListDb {

    //We have only a list of users that represents all the previous chats that we have done
    Observable<List<String>> observeConversationsFor(User user);

}
