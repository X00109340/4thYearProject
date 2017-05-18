package com.example.shaju.letzchat.private_conversations.view;

import android.view.View;
import android.widget.LinearLayout;
import android.util.AttributeSet;
import android.widget.TextView;
import android.content.Context;
import android.content.res.TypedArray;


import com.example.shaju.letzchat.R;
import com.example.shaju.letzchat.private_conversations.model.Message;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import github.ankushsachdeva.emojicon.EmojiconTextView;


//SOME CODE are taken from  Github https://github.com/novoda/bonfire-firebase-sample
public class ConversationMessageView extends LinearLayout {

    //time text view for when message was send
    private TextView timeTextView;
    //Text view for date - only used when the message date is after one day of previous message
    private TextView dateTextView;
    //Emojicon text view
    private EmojiconTextView messageTextView;



    private int layoutResId;

    public ConversationMessageView(Context contextIN, AttributeSet attributeSetIN) {
        super(contextIN, attributeSetIN);
        init(contextIN, attributeSetIN);
    }



    //	Called after a view and all of its children has been inflated from XML.
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View.inflate(getContext(), layoutResId, this);
        this.dateTextView = (TextView) this.findViewById(R.id.dateTextView);
        this.messageTextView = (EmojiconTextView) this.findViewById(R.id.message);
        this.timeTextView = (TextView) this.findViewById(R.id.time);
    }

    public void display(final Message message) {
        final String timestamp = message.getTimestamp();
        if (dateTextView != null) {
            dateTextView.setText(getDate(timestamp));
        }
        messageTextView.setText(message.getMessage());
        timeTextView.setText(getTimestamp(timestamp));
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null)
        {
            int[] attrsArray = {
                    android.R.attr.layout
            };
            TypedArray array = context.obtainStyledAttributes(attrs, attrsArray);
            layoutResId = array.getResourceId(0, R.layout.conv_msg_dest_merge);
            array.recycle();
        }
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

    //Get current time
    public static String getTimestamp(String timestampIN) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/HH/mm");
            Date date = sdf.parse(timestampIN);
            long currentDate = date.getTime();

            long milliseconds = TimeZone.getDefault().getOffset(currentDate);
            long hour = (milliseconds / (1000 * 60 * 60)) % 24;
            long minutes = (milliseconds / (1000 * 60)) % 60;

            String[] timestampPart = timestampIN.split("/");
            long h = Long.parseLong(timestampPart[3]);
            long m = Long.parseLong(timestampPart[4]);
            h += hour;
            h %= 24;
            m += minutes;
            m %= 60;

            String output = h + ":" + m;
            if (h < 10) {
                if (m < 10) {
                    output = "0" + h + ":0" + m;
                } else {
                    output = "0" + h + ":" + m;
                }
            } else if (m < 10) {
                output = h + ":0" + m;
            }
            return output;

        } catch (ParseException e) {

        }
        return null;
    }

}
