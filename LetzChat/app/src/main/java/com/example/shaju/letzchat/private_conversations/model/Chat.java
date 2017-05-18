package com.example.shaju.letzchat.private_conversations.model;


import java.util.List;


public class Chat {

    //List of messages in a chat
    private final List<Message> messageList;

    //Constructor for Chat class
    public Chat(List<Message> messageList) {
        this.messageList = messageList;
    }


    //Get the current position of the chat message
    public Message get(int position) {
        return messageList.get(position);
    }


    //Getter method to return the list of messages

    //To return the size of the messageList
    public int size() {
        return messageList.size();
    }

    //SortByDate method


    //To compare Date of messages in order to show when the last message was sent


    //Adding a new message when clicked send button
    public void addMessage(Message message) {
        if (!this.messageList.contains(message))
            this.messageList.add(message);
    }



    @Override
    public int hashCode() {
        return messageList != null ? messageList.hashCode() : 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Chat chat = (Chat) o;

        return messageList != null ? messageList.equals(chat.messageList) : chat.messageList == null;

    }

}
