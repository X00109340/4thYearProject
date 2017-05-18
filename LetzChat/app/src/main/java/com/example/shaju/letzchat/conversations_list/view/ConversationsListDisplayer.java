package com.example.shaju.letzchat.conversations_list.view;

import com.example.shaju.letzchat.conversations_list.model.ConversationModel;
import com.example.shaju.letzchat.conversations_list.model.ConversationsModel;

/**
 * Created by Shajun on 12/03/2017.
 */

public interface ConversationsListDisplayer {

    interface ConversationListInteractionListener {
        //When a conversationModel is selected
        void onConversationSelected(ConversationModel conversationModel);
    }


    //attach listener
    void attach(ConversationListInteractionListener conversationListInteractionListener);

    void detach(ConversationListInteractionListener conversationListInteractionListener);

    //Display all conversationsModel
    void display(ConversationsModel conversationsModel);

    //Add it to the displayUserDetails
    void addToDisplay(ConversationModel conversationModel);




}
