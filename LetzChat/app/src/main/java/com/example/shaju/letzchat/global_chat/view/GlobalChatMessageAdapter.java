package com.example.shaju.letzchat.global_chat.view;

import com.example.shaju.letzchat.R;
import com.example.shaju.letzchat.analytics.DeveloperError;
import com.example.shaju.letzchat.global_chat.model.Chat;
import com.example.shaju.letzchat.global_chat.model.Message;
import com.example.shaju.letzchat.users_page.model.User;
import com.example.shaju.letzchat.users_page.model.Users;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;


public class GlobalChatMessageAdapter extends RecyclerView.Adapter<GlobalChatMessageViewHolder> {

    //Create an arraylist of chat messages
    private Chat chat = new Chat(new ArrayList<Message>());

    //User details
    private User userDetails;

    //If the message owner is looking at the messages then show messages on the right hand side that were sent by this userDetails
    private static final int VIEW_TYPE_MESSAGE_THIS_USER = 0;
    private static final int VIEW_TYPE_MESSAGE_THIS_USER_OTHER_DATE = 3;

    //If it a receiver of messages
    private static final int VIEW_TYPE_MESSAGE_OTHER_USERS = 1;
    private static final int VIEW_TYPE_MESSAGE_OTHER_USERS_OTHER_DATE = 4;

    //Layout inflate
    private final LayoutInflater layoutInflater;

    //constructor
    GlobalChatMessageAdapter(LayoutInflater layoutInflaterIN) {
        layoutInflater = layoutInflaterIN;
        setHasStableIds(true);
    }

    //Add new message to global chat
    public void add(Message messageIN, User senderIN, User userDetailsIN) {
        chat.addMessage(messageIN);
        chat.addUser(senderIN);
        userDetails = userDetailsIN;
        //notify observers of new chat message
        notifyDataSetChanged();
    }

    public void update(Chat chatIN, Users usersIN, User userIN) {
        chat = chatIN;
        for (User u: usersIN.getUsers())
        {
            chat.addUser(u);
        }
        userDetails = userIN;
        //notify data changed
        notifyDataSetChanged();
    }

    //From github link:
    @Override
    public GlobalChatMessageViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewTypeIN) {
        GlobalChatPageMessageView globalChatPageMessageView;
        if (viewTypeIN == VIEW_TYPE_MESSAGE_THIS_USER)
        {
            globalChatPageMessageView = (GlobalChatPageMessageView) layoutInflater.inflate(R.layout.global_msg_view_sender, viewGroup, false);
        }
        else if (viewTypeIN == VIEW_TYPE_MESSAGE_OTHER_USERS)
        {
            globalChatPageMessageView = (GlobalChatPageMessageView) layoutInflater.inflate(R.layout.global_msg_dest, viewGroup, false);
        }
        else if (viewTypeIN == VIEW_TYPE_MESSAGE_THIS_USER_OTHER_DATE)
        {
            globalChatPageMessageView = (GlobalChatPageMessageView) layoutInflater.inflate(R.layout.global_msg_sender_date, viewGroup, false);
        }
        else if (viewTypeIN == VIEW_TYPE_MESSAGE_OTHER_USERS_OTHER_DATE)
        {
            globalChatPageMessageView = (GlobalChatPageMessageView) layoutInflater.inflate(R.layout.global_msg_dest_date, viewGroup, false);
        }
        else
        {
            throw new DeveloperError("global error");
        }
        return new GlobalChatMessageViewHolder(globalChatPageMessageView);
    }

    //return item at position - for getting time
    @Override
    public long getItemId(int position) {
        return Long.parseLong(chat.get(position).getTimestamp().replace("/",""));
    }

    //return chat length
    @Override
    public int getItemCount()
    {
        return chat.size();
    }

    /**
     *
     * Also from github: https://github.com/gerraldDoherty/firebase/example
     */

    @Override
    public int getItemViewType(int position) {
        try {
            String[] firstDate = getDate(chat.get(position - 1).getTimestamp()).split("/");
            String[] secondDate = getDate(chat.get(position).getTimestamp()).split("/");
            String concatDate1 = firstDate[0] + firstDate[1] + firstDate[2];
            String concatDate2 = secondDate[0] + secondDate[1] + secondDate[2];
            if (!concatDate1.equals(concatDate2))
            {
                return chat.get(position).getUid().equals(userDetails.getUid()) ? VIEW_TYPE_MESSAGE_THIS_USER_OTHER_DATE : VIEW_TYPE_MESSAGE_OTHER_USERS_OTHER_DATE;
            }
        } catch (ArrayIndexOutOfBoundsException e)
        {
            return chat.get(position).getUid().equals(userDetails.getUid()) ? VIEW_TYPE_MESSAGE_THIS_USER_OTHER_DATE : VIEW_TYPE_MESSAGE_OTHER_USERS_OTHER_DATE;
        }

        return chat.get(position).getUid().equals(userDetails.getUid()) ? VIEW_TYPE_MESSAGE_THIS_USER : VIEW_TYPE_MESSAGE_OTHER_USERS;
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

    @Override
    public void onBindViewHolder(GlobalChatMessageViewHolder globalChatMessageViewHolderIN, int positionIN)
    {
        Message message = chat.get(positionIN);
        if (chat.getUser(message.getUid()) != null) {
            globalChatMessageViewHolderIN.bind(chat.getUser(message.getUid()), message);
        }
    }

}
