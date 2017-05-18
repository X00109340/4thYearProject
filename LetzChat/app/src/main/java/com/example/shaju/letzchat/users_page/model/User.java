package com.example.shaju.letzchat.users_page.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.ParcelFormatException;


/**
 * Created by Shajun on 10/02/2017.
 */

public class User implements Parcelable {

    private String fcm;

    //User id
    private String uid;
    //Image
    private String image;
    //Email
    private String email;
    //User Online ID
    private String name;
    //Lase seen time
    private long lastSeen;


    //Used by Firebase
    public User() {
    }

    public User(String idIN, String nameIN, String imageIN, String emailIN) {
        uid = idIN;
        name = nameIN;
        image = imageIN;
        email = emailIN;
    }

    public User(Parcel parcelIN){
        this.uid = parcelIN.readString();
        this.image = parcelIN.readString();
        this.name = parcelIN.readString();
        this.email = parcelIN.readString();
        this.lastSeen = parcelIN.readLong();
        this.fcm = parcelIN.readString();
    }

    public void setFcm(String fcm) {
        this.fcm = fcm;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastSeenTime(long lastSeenTime) {
        this.lastSeen = lastSeenTime;
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getImage() {
        return image;
    }

    public long getLastSeen() {
        return lastSeen;
    }

    public String getFcm() {
        return fcm;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        User user = (User) o;

        if (uid != null ? !uid.equals(user.uid) : user.uid != null) {
            return false;
        }
        if (name != null ? !name.equals(user.name) : user.name != null) {
            return false;
        }
        return image != null ? image.equals(user.image) : user.image == null;

    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        try {
            parcel.writeString(this.uid);
            parcel.writeString(this.name);
            parcel.writeString(this.image);
            parcel.writeString(this.email);
            parcel.writeLong(this.lastSeen);
            parcel.writeString(this.fcm);
        } catch (ParcelFormatException e) {
            e.printStackTrace();
        }
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }
}
