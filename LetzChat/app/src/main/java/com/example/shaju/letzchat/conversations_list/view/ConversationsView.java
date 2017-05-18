package com.example.shaju.letzchat.conversations_list.view;


import android.widget.FrameLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.shaju.letzchat.ChatDependencies;
import com.example.shaju.letzchat.R;
import com.example.shaju.letzchat.conversations_list.model.ConversationModel;
import com.example.shaju.letzchat.image_storage.FirebaseImageLoaderService;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


public class ConversationsView extends FrameLayout {

    //profile image
    private CircleImageView profileImageView;
    //Online ID text view (name)
    private TextView nameTextView;
    //Last message text view
    private TextView messageTextView;
    //last message time stamp
    private TextView timeTextView;


    private int layoutResId;

    public ConversationsView(Context contextIN, AttributeSet attributeSetIN) {
        super(contextIN, attributeSetIN);
        init(contextIN, attributeSetIN);
    }

    private void init(Context contextIN, AttributeSet attributeSetIN) {
        if (attributeSetIN != null) {
            int[] attrsArray = {
                    android.R.attr.layout
            };
            TypedArray array = contextIN.obtainStyledAttributes(attributeSetIN, attrsArray);
            layoutResId = array.getResourceId(0, R.layout.conv_list_merge);
            array.recycle();
        }
    }

    //Called after a view and all of its children has been inflated from XML
    //Initialise all the text view, image and time
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View.inflate(getContext(), layoutResId, this);
        nameTextView = (TextView) this.findViewById(R.id.nameTextView);
        messageTextView = (TextView) this.findViewById(R.id.messageTextView);
        timeTextView = (TextView) this.findViewById(R.id.timeTextView);
        profileImageView = (CircleImageView) this.findViewById(R.id.profileImageView);
    }

    //Display all the conversationModel
    public void display(ConversationModel conversationModel) {
        loadImageElseBlack(conversationModel.getImage(),profileImageView,getContext());
        messageTextView.setText(conversationModel.getMessage());

        nameTextView.setText(conversationModel.getName());


        String date = getDate(conversationModel.getTime());

        String time = getTimestamp(conversationModel.getTime());
        //Current time
        String today = getCurrentTimestamp();
        //Last message received date
        String[] time1 = date.split("/");
        //Current date
        String[] time2 = today.split("/");
        if ((time1[2]+time1[1]+time1[0]).equals(time2[0]+time2[1]+time2[2]))
        {
            //Today
            timeTextView.setText(time + "\n\n" + getContext().getString(R.string.conversations_conversation_item_today));
        } else
        {
            //Set the date last message received
            timeTextView.setText(date);
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



    //Get current time. Used when sending the messages to upload the time the message was sent
    public static String getCurrentTimestamp() {
        SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyyy/MM/dd/HH/mm/ss");
        dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormatGmt.format(new Date());
    }


    //Load black person image if no profile image
    public static void loadImageElseBlack(String image, CircleImageView circleImageViewIN, Context contextIN) {

        //If user has image the
        try {
            if (image != null && image.length() > 0) {
                StorageReference ref = ChatDependencies.INSTANCE.getStorageService().getProfileImageReferenceFromStorage(image);
                Glide.with(contextIN)
                        .using(new FirebaseImageLoaderService())
                        .load(ref)
                        .into(circleImageViewIN);
            } else {
                Glide.with(contextIN)
                        .load("")
                        .placeholder(R.drawable.person_circleblack)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(circleImageViewIN);
            }
        } catch (IllegalArgumentException e) {

        }
    }

}