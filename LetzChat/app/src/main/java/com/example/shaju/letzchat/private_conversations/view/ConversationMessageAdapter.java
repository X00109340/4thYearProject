package com.example.shaju.letzchat.private_conversations.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import android.view.ViewGroup;

import com.example.shaju.letzchat.R;
import com.example.shaju.letzchat.analytics.DeveloperError;
import com.example.shaju.letzchat.private_conversations.model.Chat;
import com.example.shaju.letzchat.private_conversations.model.Message;

/*
 * Sample codes taken from https://github.com/novoda/bonfire-firebase-sample/blob/master/android/app/src/main/java/com/novoda/bonfire/chat/view/ChatAdapter.java
 */


public class ConversationMessageAdapter extends RecyclerView.Adapter<ConversationMessageViewHolder> {


    //A chat consists of an list of messages
    private Chat chat = new Chat(new ArrayList<Message>());

    //self name
    private String self;

    private static final int VIEW_TYPE_MESSAGE_THIS_USER = 0;
    private static final int VIEW_TYPE_MESSAGE_OTHER_USERS = 1;

    private static final int VIEW_TYPE_MESSAGE_THIS_USER_OTHER_DATE = 3;
    private static final int VIEW_TYPE_MESSAGE_OTHER_USERS_OTHER_DATE = 4;

    private final LayoutInflater inflater;

    ConversationMessageAdapter(LayoutInflater layoutInflaterIN)
    {
        inflater = layoutInflaterIN;
        setHasStableIds(true);
    }


    //Add new message to the chat
    public void add(Message message, String user) {
        this.self = user;
        this.chat.addMessage(message);
        notifyDataSetChanged();
    }

    //Update the chat for new messages
    public void update(Chat chat, String user) {
        this.chat = chat;
        this.self = user;
        notifyDataSetChanged();
    }

    //diffrent view types for conversation between sender and receiver
    @Override
    public ConversationMessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ConversationMessageView messageView;
        if (viewType == VIEW_TYPE_MESSAGE_THIS_USER)
        {
            messageView = (ConversationMessageView) inflater.inflate(R.layout.conv_item_dest_view, parent, false);
        } else if (viewType == VIEW_TYPE_MESSAGE_THIS_USER_OTHER_DATE)
        {
            messageView = (ConversationMessageView) inflater.inflate(R.layout.con_msg_date_destination, parent, false);
        } else if (viewType == VIEW_TYPE_MESSAGE_OTHER_USERS) {
                messageView = (ConversationMessageView) inflater.inflate(R.layout.conv_sender_msg_item, parent, false);
        } else if (viewType == VIEW_TYPE_MESSAGE_OTHER_USERS_OTHER_DATE) {
            messageView = (ConversationMessageView) inflater.inflate(R.layout.con_msg_sender_date, parent, false);
        } else {
            throw new DeveloperError("Chat Error!!!");
        }
        return new ConversationMessageViewHolder(messageView);
    }

    //return the chat list size
    @Override
    public int getItemCount() {
        return chat.size();
    }

    //
    @Override
    public long getItemId(int position) {
        return Long.parseLong(chat.get(position).getTimestamp().replace("/",""));
    }

    @Override
    public void onBindViewHolder(ConversationMessageViewHolder holder, int position) {
        holder.bind(chat.get(position));
    }



    //Code taken from above link from github
    //Used to find the view type
    @Override
    public int getItemViewType(int position) {
        try {

            String[] firstDate = getDate(chat.get(position - 1).getTimestamp()).split("/");
            String[] secondDate = getDate(chat.get(position).getTimestamp()).split("/");

            String concatDate1 = firstDate[0] + firstDate[1] + firstDate[2];
            String concatDate2 = secondDate[0] + secondDate[1] + secondDate[2];

            if (!concatDate1.equals(concatDate2))
            {
                return chat.get(position).getDestination().equals(self) ? VIEW_TYPE_MESSAGE_THIS_USER_OTHER_DATE : VIEW_TYPE_MESSAGE_OTHER_USERS_OTHER_DATE;
            }
        } catch (ArrayIndexOutOfBoundsException e)
        {
            return chat.get(position).getDestination().equals(self) ? VIEW_TYPE_MESSAGE_THIS_USER_OTHER_DATE : VIEW_TYPE_MESSAGE_OTHER_USERS_OTHER_DATE;
        }

        return chat.get(position).getDestination().equals(self) ? VIEW_TYPE_MESSAGE_THIS_USER : VIEW_TYPE_MESSAGE_OTHER_USERS;
    }



    public static String getDate(String timestampIN) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/HH/mm");
            Date date = sdf.parse(timestampIN);
            long currentDate = date.getTime();
            currentDate += TimeZone.getDefault().getOffset(currentDate);

            SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM/yyyy");
            return sdfDate.format(currentDate);
        } catch (ParseException e) {

        }
        return null;
    }
}
