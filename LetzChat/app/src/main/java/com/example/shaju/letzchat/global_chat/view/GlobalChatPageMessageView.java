package com.example.shaju.letzchat.global_chat.view;

import de.hdodenhof.circleimageview.CircleImageView;
import github.ankushsachdeva.emojicon.EmojiconTextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.shaju.letzchat.ChatDependencies;
import com.example.shaju.letzchat.global_chat.model.Message;
import com.example.shaju.letzchat.image_storage.FirebaseImageLoaderService;
import com.example.shaju.letzchat.users_page.model.User;
import com.example.shaju.letzchat.R;
import com.google.firebase.storage.StorageReference;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


public class GlobalChatPageMessageView extends LinearLayout {

    //Layout id
    private int layoutResId;

    //Time the message was sent
    private TextView timestampTextView;
    //
    private TextView nameTextView;
    //Circle image view to show sender profile picture
    private CircleImageView profileImageView;
    //Text view to show the date time only if the message is the first message on that day
    private TextView dateTextView;

    //Emojicon text view
    private EmojiconTextView emojiconMessageTextView;

    public GlobalChatPageMessageView(Context contextIN, AttributeSet attributeSetIN) {
        super(contextIN, attributeSetIN);
        init(contextIN, attributeSetIN);
        super.setOrientation(VERTICAL);
    }

    //On XML load up
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        //set the layout
        View.inflate(getContext(), layoutResId, this);

        //initialise the variables
        this.profileImageView = (CircleImageView) this.findViewById(R.id.profileImageView);
        this.emojiconMessageTextView = (EmojiconTextView) this.findViewById(R.id.messageTextView);
        this.dateTextView = (TextView) this.findViewById(R.id.dateTextView);
        this.nameTextView = (TextView) this.findViewById(R.id.messengerTextView);
        this.timestampTextView = (TextView) this.findViewById(R.id.timeTextView);

    }

    //Display user deatils - message (time, message) , user (image, name)
    public void display(User user, final Message message) {

        //Load up the image
        loadImageElseBlack(user.getImage(),profileImageView,getContext());

        //get the time the message was sent
        String timestamp = message.getTimestamp();
        if (dateTextView != null)
        {
            dateTextView.setText(getDate(timestamp));
        }
        emojiconMessageTextView.setText(message.getText());

        //Set name of sender
        nameTextView.setText(user.getName());
        //set message send time
        timestampTextView.setText(getTimestamp(timestamp));
    }

    private void init(Context contextIN, AttributeSet attributeSetIN) {
        if (attributeSetIN != null)
        {
            int[] attrsArray = {
                    android.R.attr.layout
            };
            TypedArray array = contextIN.obtainStyledAttributes(attributeSetIN, attrsArray);
            layoutResId = array.getResourceId(0, R.layout.global_msg_dest_merge);
            array.recycle();
        }
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
