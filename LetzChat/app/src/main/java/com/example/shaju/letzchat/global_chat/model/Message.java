package com.example.shaju.letzchat.global_chat.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


public class Message {

    //Message sent
    private String text;
    //Time message sent
    private String timestamp;
    //User ID
    private String uid;
    //image url
    private String imageUrl;

    //Empty constructor
    public Message() {
    }

    //Overloaded constructor
    public Message(String uidIN, String messageIN) {
        timestamp = getCurrentTimestamp();
        uid = uidIN;
        text = messageIN;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    //get message
    public String getText()
    {
        return text;
    }

    //get time
    public String getTimestamp()
    {
        return timestamp;
    }

    //get user id
    public String getUid()
    {
        return uid;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Message message = (Message) o;

        return uid != null && uid.equals(message.uid) && text != null && text.equals(message.text) && timestamp != null && timestamp.equals(message.timestamp);
    }



    //Get current time. Used when sending the messages to upload the time the message was sent
    public static String getCurrentTimestamp() {
        SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyyy/MM/dd/HH/mm/ss");
        dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormatGmt.format(new Date());
    }


}
