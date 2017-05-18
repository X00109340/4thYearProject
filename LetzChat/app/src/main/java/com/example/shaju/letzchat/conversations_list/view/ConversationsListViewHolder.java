package com.example.shaju.letzchat.conversations_list.view;

import android.support.v7.widget.RecyclerView;
import com.example.shaju.letzchat.conversations_list.model.ConversationModel;

import android.view.View;



public class ConversationsListViewHolder extends RecyclerView.ViewHolder {

    //ConversationModel view
    private final ConversationsView conversationsView;

    public ConversationsListViewHolder(ConversationsView conversationsViewIN) {
        super(conversationsViewIN);
        //initialise conversation view
        conversationsView = conversationsViewIN;
    }


    //When a conversation is selected
    public interface ConversationSelectionListener {
        void onConversationSelected(ConversationModel conversationModel);
    }

    //When a conversation is selected
    public void bind(final ConversationModel conversationModelIN, final ConversationSelectionListener conversationSelectionListenerIN) {
        conversationsView.display(conversationModelIN);
        conversationsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //CALL interface
                conversationSelectionListenerIN.onConversationSelected(conversationModelIN);
            }
        });
    }


}