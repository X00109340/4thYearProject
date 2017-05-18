package com.example.shaju.letzchat.private_conversations.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


//Message class holds the Variable for each message that is being sent (name, destination, time, message)
public class Message {

    //Sender name
    private String senderName;
    //The message that is being send
    private String message;
    //End receivers name
    private String destination;
    //The timestamp of message sent
    private String timestamp;
    //
    private String photoUrl;


    //Empty constructor
    public Message() {}

    //Overloaded constructor
    public Message(String senderNameIN, String destinationIN, String messageIN, String photoUrlIN) {
        senderName = senderNameIN;
        destination = destinationIN;
        message = messageIN;
        photoUrl = photoUrlIN;
        timestamp = getCurrentTimestamp();
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getSenderName() {
        return senderName;
    }

    //Returns the message
    public String getMessage() {
        return message;
    }

    //Returns the end receiver name
    public String getDestination() {
        return destination;
    }

    //Returns the timestamp variable value
    public String getTimestamp() {
        return timestamp;
    }



    //Get current time. Used when sending the messages to upload the time the message was sent
    public static String getCurrentTimestamp() {
        SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyyy/MM/dd/HH/mm/ss");
        dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormatGmt.format(new Date());
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

        return this.message != null && this.message.equals(message.message)
                && timestamp != null && timestamp.equals(message.timestamp);
    }
}
