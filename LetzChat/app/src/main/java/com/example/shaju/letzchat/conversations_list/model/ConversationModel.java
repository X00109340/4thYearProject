package com.example.shaju.letzchat.conversations_list.model;

import android.os.ParcelFormatException;
import android.os.Parcelable;
import android.os.Parcel;




public class ConversationModel implements Parcelable {

    //User unique id
    private String userID;
    //user image
    private String userImage;
    //user name
    private String userName;
    //user message
    private String userMessage;
    //user message time sent
    private String userTime;


    public ConversationModel(String userIDIN, String userNameIN, String userImageIN, String userMessageIN, String userTimeIN) {
        userID = userIDIN;
        userName = userNameIN;
        userImage = userImageIN;
        userMessage = userMessageIN;
        userTime = userTimeIN;
    }

    public ConversationModel(Parcel in){
        this.userID = in.readString();
        this.userName = in.readString();
        this.userImage = in.readString();
        this.userMessage = in.readString();
        this.userTime = in.readString();
    }

    //return id
    public String getUid() {
        return userID;
    }
    //return image
    public String getImage() {
        return userImage;
    }
    //return name
    public String getName() {
        return userName;
    }
    //return message
    public String getMessage() {
        return userMessage;
    }
    //return time
    public String getTime() {
        return userTime;
    }

    //Not used
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ConversationModel conversationModel = (ConversationModel) o;

        return userID != null && userID.equals(conversationModel.userID);
    }



    @Override
    public void writeToParcel(Parcel parcel, int i) {
        try {
            parcel.writeString(this.userID);
            parcel.writeString(this.userName);
            parcel.writeString(this.userImage);
            parcel.writeString(this.userMessage);
            parcel.writeString(this.userTime);
        } catch (ParcelFormatException e) {
            e.printStackTrace();
        }
    }

    //Not used
    @Override
    public int hashCode() {
        int answer = userID != null ? userID.hashCode() : 0;
        answer = 31 * answer;
        return answer;
    }


    //Delete - Not needed or used
    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public ConversationModel createFromParcel(Parcel in) {
            return new ConversationModel(in);
        }

        public ConversationModel[] newArray(int size) {
            return new ConversationModel[size];
        }
    };


}