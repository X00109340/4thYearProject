package com.example.shaju.letzchat.users_page.database;

import com.example.shaju.letzchat.rx.FirebaseObservableListeners;
import com.example.shaju.letzchat.users_page.model.User;
import com.example.shaju.letzchat.users_page.model.Users;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import rx.Observable;
import rx.functions.Func1;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Shajun on 10/02/2017.
 */

public class FBUserDatabase implements UserDatabase {

    //Listners
    private final FirebaseObservableListeners firebaseObservableListeners;

    //Firebase database reference
    private final DatabaseReference databaseReference;


    public FBUserDatabase(FirebaseDatabase firebaseDatabaseIN, FirebaseObservableListeners firebaseObservableListenersIN) {
        databaseReference = firebaseDatabaseIN.getReference("users");
        this.firebaseObservableListeners = firebaseObservableListenersIN;
    }

    //SET IMAGE
    @Override
    public void setNewUserImage(String userIdIN, String imageIN) {
        databaseReference.child(userIdIN).child("image").setValue(imageIN);
    }

    //SET USER NAME
    @Override
    public void setNewOnlineId(String userIdIN, String nameIN) {
        databaseReference.child(userIdIN).child("name").setValue(nameIN);
    }

    //SET USER NAME
    @Override
    public void setUserPassword(String userIdIN, String passowrdIN) {
        databaseReference.child(userIdIN).child("password").setValue(passowrdIN);
    }

    //SET LAST SEEN
    @Override
    public void setUserLastOnline(String userIdIN) {
        DatabaseReference lastSeenRef = databaseReference.child(userIdIN).child("lastSeen");
        lastSeenRef.setValue(0);
        lastSeenRef.onDisconnect().removeValue();
        lastSeenRef.onDisconnect().setValue(ServerValue.TIMESTAMP);
    }

    //Observe users
    @Override
    public Observable<Users> observeAllUsers() {
        return firebaseObservableListeners.listenToValueEvents(databaseReference, toUsers());
    }

    @Override
    public Observable<User> observeSpecificUser(String userId) {
        return firebaseObservableListeners.listenToValueEvents(databaseReference.child(userId), as(User.class));
    }

    @Override
    public Observable<User> readUserFrom(String userId) {
        return firebaseObservableListeners.listenToSingleValueEvents(databaseReference.child(userId), as(User.class));
    }

    @Override
    public Observable<Boolean> initialiseUserLastOnline() {
        DatabaseReference amOnline = databaseReference.getParent().child(".info").child("connected");
        return firebaseObservableListeners.listenToValueEvents(amOnline,lastSeenHandler());
    }


    @Override
    public Observable<Users> singleObserveUsers() {
        return firebaseObservableListeners.listenToSingleValueEvents(databaseReference, toUsers());
    }


    private Func1<DataSnapshot, Boolean> lastSeenHandler() {
        return new Func1<DataSnapshot, Boolean>() {
            @Override
            public Boolean call(DataSnapshot dataSnapshot) {
                return dataSnapshot.getValue(Boolean.class);
            }
        };
    }


    private Func1<DataSnapshot, Users> toUsers() {
        return new Func1<DataSnapshot, Users>() {
            @Override
            public Users call(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                List<User> users = new ArrayList<>();
                for (DataSnapshot child : children) {
                    User message = child.getValue(User.class);
                    users.add(message);
                }
                return new Users(users);
            }
        };
    }

    private <T> Func1<DataSnapshot, T> as(final Class<T> tClass) {
        return new Func1<DataSnapshot, T>() {
            @Override
            public T call(DataSnapshot dataSnapshot) {
                return dataSnapshot.getValue(tClass);
            }
        };
    }
}