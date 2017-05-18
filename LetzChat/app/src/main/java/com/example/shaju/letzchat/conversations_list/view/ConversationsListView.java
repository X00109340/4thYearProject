package com.example.shaju.letzchat.conversations_list.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.example.shaju.letzchat.R;
import com.example.shaju.letzchat.conversations_list.model.ConversationModel;
import com.example.shaju.letzchat.conversations_list.model.ConversationsModel;

/**
 * Created by Shajun on 12/03/2017.
 */

public class ConversationsListView extends LinearLayout implements ConversationsListDisplayer {

    //List adapter
    private final ConversationsListAdapter conversationsListAdapter;
    //Interatcion listener
    private ConversationListInteractionListener conversationListInteractionListener;

    public ConversationsListView(Context contextIN, AttributeSet attributeSetIN) {
        super(contextIN, attributeSetIN);
        setOrientation(VERTICAL);
        conversationsListAdapter = new ConversationsListAdapter(LayoutInflater.from(contextIN));
    }

    //Attach listener
    @Override
    public void attach(ConversationListInteractionListener conversationListInteractionListenerIN) {
        conversationListInteractionListener = conversationListInteractionListenerIN;
        conversationsListAdapter.attach(conversationListInteractionListener);
    }

    //Detach listener
    @Override
    public void detach(ConversationListInteractionListener conversationListInteractionListenerIN) {
        conversationsListAdapter.detach(conversationListInteractionListenerIN);
        conversationListInteractionListener = null;
    }

    //Called after a view and all of its children has been inflated from XML
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View.inflate(getContext(), R.layout.con_list_merg, this);
        RecyclerView conversations = (RecyclerView) this.findViewById(R.id.conversationsRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        conversations.setLayoutManager(layoutManager);
        conversations.setAdapter(conversationsListAdapter);
    }

    //Display conversationsModel
    @Override
    public void display(ConversationsModel conversationsModel) {
        conversationsListAdapter.update(conversationsModel);
    }

    //add
    @Override
    public void addToDisplay(ConversationModel conversationModel) {
        conversationsListAdapter.add(conversationModel);
    }



}