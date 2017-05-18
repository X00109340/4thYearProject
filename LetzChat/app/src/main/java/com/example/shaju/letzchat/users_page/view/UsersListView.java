package com.example.shaju.letzchat.users_page.view;

import com.example.shaju.letzchat.R;
import com.example.shaju.letzchat.users_page.model.Users;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.LinearLayout;

/**
 * Created by Shajun on 12/02/2017.
 */

/**
 * Based on from: https://github.com/novoda/bonfire-firebase-sample/blob/master/android/app/src/main/java/com/novoda/bonfire/user/view/UsersView.java
 */
public class UsersListView extends LinearLayout implements UsersListDisplayer {

    //Filtered adapter
    private UsersListAdapter usersFilteredAdapter;
    //listener
    private OnUserClickInteractionListener usersInteractionListener;
    //User list adapter
    private final UsersListAdapter usersListAdapter;

    //Recycler view
    private RecyclerView conversations;

    //Constructor
    public UsersListView(Context contextIN, AttributeSet attributeSetIN) {
        super(contextIN, attributeSetIN);
        setOrientation(VERTICAL);

        usersFilteredAdapter = new UsersListAdapter(LayoutInflater.from(contextIN));
        usersListAdapter = new UsersListAdapter(LayoutInflater.from(contextIN));
    }

    //On XML load up
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        //set view
        View.inflate(getContext(), R.layout.con_list_merg, this);

        //Initialise variables with findviewbyID
        conversations = (RecyclerView) this.findViewById(R.id.conversationsRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        conversations.setLayoutManager(layoutManager);
        conversations.setAdapter(usersListAdapter);

    }

    //Attach listeners
    @Override
    public void attach(OnUserClickInteractionListener onUserClickInteractionListenerIN) {
        this.usersInteractionListener = onUserClickInteractionListenerIN;
        usersListAdapter.attach(onUserClickInteractionListenerIN);
        usersFilteredAdapter.attach(usersInteractionListener);
    }

    //detach listeners
    @Override
    public void detach(OnUserClickInteractionListener onUserClickInteractionListenerIN) {
        this.usersInteractionListener = null;
        usersListAdapter.detach(onUserClickInteractionListenerIN);
        usersFilteredAdapter.detach(onUserClickInteractionListenerIN);
    }


    //Display the user
    @Override
    public void displayAllUsers(Users usersIN) {
        usersListAdapter.update(usersIN);
    }

    //Filter user based on id
    @Override
    public void filteredSearch(String idIN) {
        //if nothing entered
        if (idIN.equals(""))
        {
            conversations.setAdapter(usersListAdapter);
        }
        //else
        else
        {
            usersFilteredAdapter.update(usersListAdapter.getUsers());
            usersFilteredAdapter.filter(idIN);
            conversations.setAdapter(usersFilteredAdapter);
        }
    }
}
