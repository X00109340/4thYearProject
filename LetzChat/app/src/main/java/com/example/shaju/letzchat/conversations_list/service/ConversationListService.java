package com.example.shaju.letzchat.conversations_list.service;

import com.example.shaju.letzchat.private_conversations.model.Message;
import com.example.shaju.letzchat.users_page.model.User;

import rx.Observable;
import java.util.List;


/**
 * Created by Shajun on 12/03/2017.
 */

public interface ConversationListService {

    Observable<List<String>> getConversationsFor(User user);


    Observable<Message> getLastMessageFor(User self, User destination);



}
