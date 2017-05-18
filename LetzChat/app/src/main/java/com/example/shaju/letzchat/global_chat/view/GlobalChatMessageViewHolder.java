package com.example.shaju.letzchat.global_chat.view;

import com.example.shaju.letzchat.global_chat.model.Message;
import com.example.shaju.letzchat.users_page.model.User;

import android.support.v7.widget.RecyclerView;


public class GlobalChatMessageViewHolder extends RecyclerView.ViewHolder {

    //Global messsage view
    private final GlobalChatPageMessageView globalChatPageMessageView;

    //Constructor
    public GlobalChatMessageViewHolder(GlobalChatPageMessageView globalChatPageMessageViewIN)
    {
        super(globalChatPageMessageViewIN);
        this.globalChatPageMessageView = globalChatPageMessageViewIN;
    }

    //**** NOT USED ****
    public void bind(User userIN, Message messageIN)
    {
        globalChatPageMessageView.display(userIN, messageIN);
    }
}
