package com.example.shaju.letzchat.conversations_list.database;


import com.example.shaju.letzchat.rx.FirebaseObservableListeners;
import com.example.shaju.letzchat.users_page.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import rx.Observable;
import rx.functions.Func1;
import java.util.List;



/**
 * Created by Shajun on 11/03/2017.
 */

public class FirebaseConversationListDb implements ConversationListDb {

    //FirebaseObservableListeners
    private FirebaseObservableListeners firebaseObservableListeners;

    //Firebase Database reference
    private DatabaseReference firebaseDatabaseReference;

    //Constructor for database class
    public FirebaseConversationListDb(FirebaseDatabase firebaseDatabaseReference, FirebaseObservableListeners firebaseObservableListeners) {
        this.firebaseDatabaseReference = firebaseDatabaseReference.getReference();
        this.firebaseObservableListeners = firebaseObservableListeners;
    }

    //Override
    @Override
    public Observable<List<String>> observeConversationsFor(User user) {
        return firebaseObservableListeners.listenToValueEvents(firebaseDatabaseReference
            .child("chat").child(user.getUid()), getConversations());
    }

    private Func1<DataSnapshot, List<String>> getConversations() {
        return new Func1<DataSnapshot, List<String>>() {
            @Override
            public List<String> call(DataSnapshot dataSnapshot) {
                final List<String> conversationsUserUid = new ArrayList<>();
                if (dataSnapshot.hasChildren()) {
                    Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                    for (final DataSnapshot child : children) {
                        conversationsUserUid.add(child.getKey());
                    }
                }
                return conversationsUserUid;
            }
        };
    }

//    private static Func1<DataSnapshot, User> asUser() {
//        return new Func1<DataSnapshot, User>() {
//            @Override
//            public User call(DataSnapshot dataSnapshot) {
//                return dataSnapshot.getValue(User.class);
//            }
//        };
//    }

}
