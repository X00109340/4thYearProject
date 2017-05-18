package com.example.shaju.letzchat.main_page.database;

import rx.functions.Func1;
import rx.Observable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.example.shaju.letzchat.users_page.model.User;
import com.example.shaju.letzchat.rx.FirebaseObservableListeners;



/**
 * Created by Shajun on 16/03/2017.
 */

public class FirebaseCloudMessagingTokenDatabase implements CloudMessagingTokenDatabase {

    //Firebase token per user
    private final String firebaseToken;

    //Event listeners
    private final FirebaseObservableListeners firebaseObservableListeners;

    //Firebase database reference
    private final DatabaseReference databaseReference;



    public FirebaseCloudMessagingTokenDatabase(FirebaseDatabase firebaseDatabaseIN, FirebaseObservableListeners firebaseObservableListenersIN, String firebaseTokenIN) {
        this.databaseReference = firebaseDatabaseIN.getReference("fcm");
        this.firebaseToken = firebaseTokenIN;
        this.firebaseObservableListeners = firebaseObservableListenersIN;

    }


    //Enable tokens
    @Override
    public void enableUserToken(String userIdIN) {
        //Boolean set to true
        databaseReference.child(userIdIN + "/" + "enabled").setValue(Boolean.TRUE.toString());
    }

    //Disable tokens
    @Override
    public void disableUserToken(String userIdIN) {
        //Boolean set to false
        databaseReference.child(userIdIN + "/" + "enabled").setValue(Boolean.FALSE.toString());
    }


    //Read the token for the user
    @Override
    public Observable<String> readUserToken(User userIN)
    {
        //return the token for the user from database
        return firebaseObservableListeners.listenToValueEvents(databaseReference.child(userIN.getUid()),asString());
    }

    //Token for each user
    @Override
    public void setUserToken(User userIN)
    {
        //Set the token value for the user
        databaseReference.child(userIN.getUid() + "/" + "token").setValue(firebaseToken);
        //Enable FCM (Firebase Cloud Messaging)
        databaseReference.child(userIN.getUid() + "/" + "enabled").setValue(Boolean.TRUE.toString());
    }


    private static Func1<DataSnapshot, String> asString() {
        return new Func1<DataSnapshot, String>() {
            @Override
            public String call(DataSnapshot dataSnapshot) {
                return dataSnapshot.child("token").getValue(String.class);
            }
        };
    }
}
