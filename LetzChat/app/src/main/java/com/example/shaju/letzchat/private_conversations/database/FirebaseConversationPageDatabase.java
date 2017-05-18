package com.example.shaju.letzchat.private_conversations.database;

import rx.functions.Func1;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import com.example.shaju.letzchat.private_conversations.model.Chat;
import com.example.shaju.letzchat.private_conversations.model.Message;
import com.example.shaju.letzchat.rx.FirebaseObservableListeners;



public class FirebaseConversationPageDatabase implements ConversationPageDatabase {


    private final DatabaseReference userChat;

    private final FirebaseObservableListeners firebaseObservableListeners;

    public FirebaseConversationPageDatabase(FirebaseDatabase firebaseDatabase, FirebaseObservableListeners firebaseObservableListeners) {
        userChat = firebaseDatabase.getReference("chat");
        this.firebaseObservableListeners = firebaseObservableListeners;
    }



    //Add message event
    @Override
    public Observable<Message> observableAddMessage(String sender, String destination) {
        return firebaseObservableListeners.listenToAddChildEvents(messagesOfUser(sender,destination), toMessage());
    }

    //New message event
    private DatabaseReference messagesOfUser(String sender, String destination) {
        return userChat.child(sender).child(destination).child("messages");
    }

    @Override
    public Observable<Message> observableLastMessage(String sender, String destination) {
        return firebaseObservableListeners.listenToSingleValueEvents(messagesOfUser(sender,destination).limitToLast(1), toLastMessage());
    }


    @Override
    public Observable<Chat> observableChat(String sender, String destination) {
        return firebaseObservableListeners.listenToValueEvents(messagesOfUser(sender,destination).limitToLast(1000), toChat());
    }

    //Send new message event
    @Override
    public void sendMessage(final String user, final Message message) {
        userChat.child(user).child(message.getDestination()).child("messages").push().setValue(message);
        userChat.child(message.getDestination()).child(user).child("messages").push().setValue(message);
    }

    //Boolean Typing event
    @Override
    public Observable<Boolean> observableTyping(String sender, String destination) {
        return firebaseObservableListeners.listenToValueEvents(userChat.child(destination).child(sender).child("typing"), asBoolean());
    }

    //SetTyping event
    @Override
    public void setTyping(String sender, String destination, Boolean value) {
        userChat.child(sender).child(destination).child("typing").setValue(value);
    }

    private Func1<DataSnapshot, Chat> toChat() {
        return new Func1<DataSnapshot, Chat>() {
            @Override
            public Chat call(DataSnapshot dataSnapshot)
            {
                //
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                //Initialiase a list of me
                List<Message> messages = new ArrayList<>();
                for (DataSnapshot child : children)
                {
                    Message message = child.getValue(Message.class);
                    messages.add(message);
                }
                return new Chat(messages);
            }
        };
    }

    private Func1<DataSnapshot, Message> toLastMessage() {
        return new Func1<DataSnapshot, Message>() {
            @Override
            public Message call(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                List<Message> messages = new ArrayList<>();
                for (DataSnapshot child : children) {
                    Message message = child.getValue(Message.class);
                    messages.add(message);
                }
                return messages.get(0);
            }
        };
    }

    private Func1<DataSnapshot, Message> toMessage() {
        return new Func1<DataSnapshot, Message>() {
            @Override
            public Message call(DataSnapshot dataSnapshot) {
                return dataSnapshot.getValue(Message.class);
            }
        };
    }





    private Func1<DataSnapshot,Boolean> asBoolean() {
        return new Func1<DataSnapshot, Boolean>() {
            @Override
            public Boolean call(DataSnapshot dataSnapshot) {
                return dataSnapshot.getValue(Boolean.class);
            }
        };
    }
}
