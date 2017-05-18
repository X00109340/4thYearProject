package com.example.shaju.letzchat.global_chat.database;

import com.example.shaju.letzchat.global_chat.model.Chat;
import com.example.shaju.letzchat.global_chat.model.Message;
import com.example.shaju.letzchat.rx.FirebaseObservableListeners;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import rx.Observable;
import rx.functions.Func1;

import java.util.ArrayList;
import java.util.List;

public class FirebaseGlobalChatPageDatabase implements GlobalChatPageDatabase {

    //Firebase listeners for changes
    private final FirebaseObservableListeners firebaseObservableListeners;

    //Database reference for global chat messages
    private final DatabaseReference databaseReference;

    //Constructor
    public FirebaseGlobalChatPageDatabase(FirebaseDatabase firebaseDatabaseIN, FirebaseObservableListeners firebaseObservableListenersIN) {
        databaseReference = firebaseDatabaseIN.getReference("globalmessages");
        this.firebaseObservableListeners = firebaseObservableListenersIN;
    }

    //Send a new message with message details - user, message
    @Override
    public void sendMessage(Message message)
    {
        databaseReference.push().setValue(message);
    }

    //1000 is the maximum limit per message
    @Override
    public Observable<Message> observeAddMessage() {
        return firebaseObservableListeners.listenToAddChildEvents(databaseReference.limitToLast(1000), toMessage());
    }

    //1000 is the maximum limit per message
    @Override
    public Observable<Chat> observeChat() {
        return firebaseObservableListeners.listenToSingleValueEvents(databaseReference.limitToLast(1000), toChat());
    }

    private Func1<DataSnapshot, Message> toMessage()
    {
        return new Func1<DataSnapshot, Message>()
        {
            @Override
            public Message call(DataSnapshot dataSnapshot) {
                return dataSnapshot.getValue(Message.class);
            }
        };
    }

    private Func1<DataSnapshot, Chat> toChat() {
        return new Func1<DataSnapshot, Chat>() {
            @Override
            public Chat call(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                List<Message> messages = new ArrayList<>();
                for (DataSnapshot child : children) {
                    Message message = child.getValue(Message.class);
                    messages.add(message);
                }
                return new Chat(messages).sortedByDate();
            }
        };
    }


}
