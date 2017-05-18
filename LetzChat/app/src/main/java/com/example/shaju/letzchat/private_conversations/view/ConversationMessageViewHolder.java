package com.example.shaju.letzchat.private_conversations.view;

import com.example.shaju.letzchat.private_conversations.model.Message;

import android.support.v7.widget.RecyclerView;




class ConversationMessageViewHolder extends RecyclerView.ViewHolder {

    private final ConversationMessageView conversationView;

    public ConversationMessageViewHolder(ConversationMessageView messageView) {
        super(messageView);
        this.conversationView = messageView;
    }

    public void bind(Message message) {
        conversationView.display(message);
    }
}
