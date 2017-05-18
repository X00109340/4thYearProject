package com.example.shaju.letzchat.conversations_list.model;

import java.util.Comparator;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;


public class ConversationsModel {

    //We have a list of conversationModelList
    private final List<ConversationModel> conversationModelList;

    //Constructor -
    public ConversationsModel(List<ConversationModel> conversationModelListIN)
    {
        this.conversationModelList = conversationModelListIN;
    }

    //Add new conversationModel
    public void add(ConversationModel conversationModel) {
        //if conversationModel does not exist then
        if (!conversationModelList.contains(conversationModel)) {
            conversationModelList.add(conversationModel);
        } else {
            conversationModelList.set(conversationModelList.indexOf(conversationModel), conversationModel);
        }
    }

    //Sort the conversation by date ... newest first
    public ConversationsModel sortedByDate() {
        List<ConversationModel> sortedList = new ArrayList<>(conversationModelList);
        Collections.sort(sortedList,byDate());
        return new ConversationsModel(sortedList);
    }

    //To return conversations that is last
    public ConversationModel getConversationAt(int position) {
        return conversationModelList.get(position);
    }

    //return list size / length
    public int size() {
        return conversationModelList.size();
    }

    //To comapre previous date and current date
    private static Comparator<? super ConversationModel> byDate() {
        return new Comparator<ConversationModel>() {
            @Override
            public int compare(ConversationModel o1, ConversationModel o2) {
                return o2.getTime().compareTo(o1.getTime());
            }
        };
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ConversationsModel conversationsModel1 = (ConversationsModel) o;

        return conversationModelList != null ? conversationModelList.equals(conversationsModel1.conversationModelList) : conversationsModel1.conversationModelList == null;
    }


}
